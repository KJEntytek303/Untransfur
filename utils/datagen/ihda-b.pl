#!/usr/bin/perl
#IHDa-b - I Hate Datagen for blocks - custom data generator for minecraft registry objects.

#Blocks:
#	-i --implicit - enable implicit rules
#	-- - end datagen params
#
#	Implicit rules:
#	if registered using "/registerWithItem\h*(/" method, append it to item registry as a block
#	implicitly generate translation
#
#	Generate blockstates/model presets ( -b / --bs= ), ( -t / --txt= )
#	Implicit rules:
#	if no --bs is specified, assume --bs=all
#	/_BUTTON$/, --bs=button
#	/_DOOR$/, --bs=door
#	/_FENCE$/,  --bs=fence
#	/_FENCE_GATE$/, --bs=gate
#	/_LOG$/, --bs=log
#	/_SIGN$/, --bs=sign
#	/_SLAB$/, --bs=slab
#	/_STAIRS$/, --bs=stairs
#	/_TRAPDOOR$/, --bs=trapdoor
#	/_WALL$/, --bs=wall
#
#	--bs=all - cubeAll model
#	--bs=axis - unspecified
#	--bs=button -
#	--bs=custom - force disable --implicit for --bs option. Ignored
#	--bs=directional - imagine piston. Takes arguments: front, side, top
#	--bs=door 
#	--bs=fence
#	--bs=gate - fence gate
#	--bs=gt_io - --directional --txt=<block_id>_front,rest,rest - Takes arguments: rest
#	--bs=horizontal - same as --directional, but optimizes out unused rotation
#	--bs=log -
#	--ps=pane
#	--bs=presplate
#	--bs=sign
#	--bs=slab
#	--bs=stairs
#	--bs=trapdoor
#	--bs=wall
#
#	--txt=texture1,texture2,texture3 - some commands accept texture arguments
#

#	Generate block lootTables ( -l --lt= )
#	Implicit Rules:
#	default is --lt=self
#
#	--lt=custom
#	--lt=ore
#	--lt=raw_ore
#	--lt=self
#
#	--arg=item,amount

#Generate GTMPG block tags
#--tags=<tag>,<tag>,<tag>
