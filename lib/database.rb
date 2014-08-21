# encoding: utf-8

require 'sequel'
require 'yaml'

# Database abstraction.
class Database
  def connect
    path = File.expand_path(
      File.join(
        File.expand_path(File.dirname(__FILE__)),
        '../db/config.yml'
      )
    )
    Sequel.connect(
      ENV['DATABASE_URL'] || YAML.load_file(path)['dev']
    )
  end
end
