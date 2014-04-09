require 'rubygems'
require 'neography'

@neo = Neography::Rest.new

#delete everything in the DB for the next run
@neo.execute_query("MATCH (n) OPTIONAL MATCH (n)-[r]->() DELETE n, r")

#insert some data
@neo.execute_query("create  ({name:'Hello'})-[:CRUEL]->({name:'World'})")

#retrieve some data
@neo.execute_query("match (s)-[r]->(e) return s.name as start, type(r), e.name as end")['data'].each do |row|
	puts "result: #{row[0]} #{row[1]} #{row[2]}"
end