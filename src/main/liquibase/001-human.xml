# encoding: utf-8

require 'sequel'

Sequel.migration do
  up do
    execute '''
    CREATE TYPE gender AS ENUM (\'male\', \'female\', \'third\');
    '''
    execute '''
    CREATE TABLE human (
      fb INTEGER UNIQUE NOT NULL,
      name VARCHAR(40) UNIQUE NOT NULL,
      age INTEGER NOT NULL,
      sex gender NOT NULL,
      photo BYTEA NOT NULL,
      language CHAR(2) NOT NULL
    )
    '''
  end

  down do
    execute 'DROP TABLE human'
  end
end
