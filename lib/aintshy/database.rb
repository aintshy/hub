# encoding: utf-8

require 'sequel'
require 'yaml'

# Database abstraction.
class Database
  def connect
    Sequel.connect(
      ENV['DATABASE_URL']
    )
  end
end
