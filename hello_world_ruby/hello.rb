require 'rubygems' 
require 'bundler/setup'
require 'neography'

@neo = Neography::Rest.new

#delete everything in the DB for the next run
@neo.commit_transaction("MATCH (n) OPTIONAL MATCH (n)-[r]->() DELETE n, r")

#insert some data
@neo.commit_transaction("create  ({name:'Hello'})-[:CRUEL]->({name:'World'})")

#retrieve some data
@neo.commit_transaction("match (s)-[r]->(e) return s.name as start, type(r), e.name as end")['results'][0]['data'][0]['row'].each do |value|
	puts "Got column: #{value}"
end