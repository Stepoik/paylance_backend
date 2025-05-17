-- Создание таблицы projects
CREATE TABLE projects (
                        id VARCHAR(36) PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT NOT NULL,
                        budget DOUBLE PRECISION NOT NULL,
                        deadline TIMESTAMP NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP NOT NULL,
                        owner_id VARCHAR(36) NOT NULL
);

-- Создание таблицы project_responses
CREATE TABLE project_responses (
                                 id VARCHAR(36) PRIMARY KEY,
                                 project_id VARCHAR(36) NOT NULL,
                                 freelancer_id VARCHAR(36) NOT NULL,
                                 proposal TEXT NOT NULL,
                                 price DOUBLE PRECISION NOT NULL,
                                 status VARCHAR(20) NOT NULL,
                                 created_at TIMESTAMP NOT NULL
);
