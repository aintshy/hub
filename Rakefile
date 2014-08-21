# encoding: utf-8

require 'rubygems'
require 'rake'
require 'rdoc'
require 'rake/clean'

CLEAN = FileList['coverage', 'rdoc']

def name
  @name = 'aintshy'
end

def version
  @version = '0.1'
end

task default: [:clean, :test, :rubocop]

require 'rake/testtask'
desc 'Run all unit tests'
Rake::TestTask.new(:test) do |test|
  test.libs << 'lib' << 'test'
  test.pattern = 'test/**/test_*.rb'
  test.verbose = false
end

require 'rubocop/rake_task'
desc 'Run RuboCop on all directories'
RuboCop::RakeTask.new(:rubocop) do |task|
  task.fail_on_error = true
  task.requires << 'rubocop-rspec'
end
