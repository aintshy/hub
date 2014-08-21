# encoding: utf-8

require 'aintshy/database'
require 'sinatra'

set :erb, content_type: 'text/xml'

db = Database.new.connect

get '/' do
  erb :index
end

db.disconnect
