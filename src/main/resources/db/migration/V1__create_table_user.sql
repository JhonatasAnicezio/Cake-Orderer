CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
)

INSERT INTO (name, email, password, role)
SELECT 'Super Admin', 'superadmin@email.com', '123456789', 'SUPER_ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'superadmin@email.com'
);