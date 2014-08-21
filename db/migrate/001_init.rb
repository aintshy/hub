# encoding: utf-8

Sequel.migration do
  up do
    execute '''
    CREATE TABLE user (
      fb INTEGER UNIQUE NOT NULL,
      name VARCHAR(40) UNIQUE NOT NULL,
      age INTEGER NOT NULL,
      sex AS ENUM ("male", "female") NOT NULL,
      photo BYTEA NOT NULL,
      language CHAR(2) NOT NULL
    )
    '''
  end

  down do
    execute 'DROP TABLE user'
  end
end
