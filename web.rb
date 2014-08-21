# encoding: utf-8

require_relative 'lib/database'
require 'sinatra'

set :erb, content_type: 'text/xml'

db = Database.new.connect

get '/' do
  erb :index
end

db.disconnect
