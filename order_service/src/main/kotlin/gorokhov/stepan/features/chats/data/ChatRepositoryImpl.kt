package gorokhov.stepan.features.chats.data

import gorokhov.stepan.features.chats.data.database.Chats
import gorokhov.stepan.features.chats.data.database.Messages
import gorokhov.stepan.features.chats.data.mappers.toChat
import gorokhov.stepan.features.chats.data.mappers.toMessage
import gorokhov.stepan.features.chats.domain.models.Chat
import gorokhov.stepan.features.chats.domain.models.CreateChat
import gorokhov.stepan.features.chats.domain.models.Message
import gorokhov.stepan.features.chats.domain.repositories.ChatRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ChatRepositoryImpl : ChatRepository {
    override suspend fun createChat(chat: CreateChat): Unit = dbQuery {
        Chats.insert {
            it[id] = chat.id
            it[projectId] = chat.projectId
            it[clientId] = chat.clientId
            it[freelancerId] = chat.freelancerId
            it[title] = chat.title
        }
    }

    override suspend fun getChatById(chatId: String): Chat? = dbQuery {
        val lastMessage = getLastMessage(chatId)

        val unreadCount = getChatUnreadCount(chatId)

        Chats.selectAll().where { Chats.id eq chatId }.firstOrNull()?.toChat(lastMessage, unreadCount = unreadCount.toInt())
    }

    override suspend fun getChatsByUserId(userId: String, offset: Long, limit: Long): List<Chat> = dbQuery {
        Chats.selectAll()
            .where { (Chats.clientId eq userId) or (Chats.freelancerId eq userId) }
            .limit(limit.toInt())
            .offset(offset)
            .map { row ->
                val chatId = row[Chats.id]
                val lastMessage = getLastMessage(chatId)

                val unreadCount = getChatUnreadCount(chatId)

                row.toChat(lastMessage, unreadCount.toInt())
            }
    }

    override suspend fun getChatByProjectId(projectId: String): Chat? = dbQuery {
        Chats.selectAll()
            .where { Chats.projectId eq projectId }
            .limit(1).firstNotNullOfOrNull { row ->
                val chatId = row[Chats.id]
                val lastMessage = getLastMessage(chatId)

                val unreadCount = getChatUnreadCount(chatId)

                row.toChat(lastMessage, unreadCount.toInt())
            }
    }

    override suspend fun getMessagesByChatId(chatId: String, offset: Long, limit: Long): List<Message> = dbQuery {
        Messages.selectAll()
            .where { Messages.chatId eq chatId }
            .orderBy(Messages.createdAt, SortOrder.ASC)
            .limit(limit.toInt())
            .offset(offset)
            .map { it.toMessage() }
    }

    override suspend fun createMessage(message: Message): Message = dbQuery {
        Messages.insert {
            it[id] = message.id
            it[chatId] = message.chatId
            it[senderId] = message.senderId
            it[text] = message.text
            it[createdAt] = message.createdAt
            it[isRead] = message.isRead
        }
        message
    }

    override suspend fun markMessagesAsRead(chatId: String, userId: String): Unit = dbQuery {
        Messages.update({
            (Messages.chatId eq chatId) and (Messages.senderId neq userId) and (Messages.isRead eq false)
        }) {
            it[isRead] = true
        }
    }

    override suspend fun getUnreadCount(chatId: String, userId: String): Int = dbQuery {
        Messages.selectAll()
            .where { (Messages.chatId eq chatId) and (Messages.senderId neq userId) and (Messages.isRead eq false) }
            .count().toInt()
    }

    private fun getChatUnreadCount(chatId: String): Long {
        return Messages.selectAll()
            .where { (Messages.chatId eq chatId) and (Messages.isRead eq false) }
            .count()
    }

    private fun getLastMessage(chatId: String): Message? {
        return Messages.selectAll()
            .where { Messages.chatId eq chatId }
            .orderBy(Messages.createdAt, SortOrder.DESC)
            .limit(1)
            .map { it.toMessage() }
            .firstOrNull()
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
