#!/usr/bin/perl

use strict;
use warnings;
use FindBin ( qw($RealBin) );
use lib ( $RealBin );
use kgetopt qw(:default)

#IHDa-I - I Hate Datagen for Items

# -- - end 
# -g --tags=<tag1>,<tag2>
# -l --layers=<number> , -i assumes 1
# -t --txt=<last-layer>, ... , <first-layer>
# -c --custom-model

my $MODPATH = "../../src/main/java/net/kjentytek303/untransfur";
my $MODID = "untransfur";
my $item_id = "";
my $args = "";

my $init_file = $MODPATH . "/init/InitItems.java"

open( my $RegFILE, "<", $MODPATH ) or die "Cannot open file $init_file: $!";

my $mode = ""

my $validator_hash_definition = {
	"layers" => '^\d$',
	"txt" => '(^[a-z0-9_/]+,)+$',
	"custom" => '',
	'implicit' => '',
	'no-implicit' => ''
};

my $short_hash_definition = {
	"l" => 'layers',
	't' => 'txt',
	'c' => 'custom',
	'i' => 'implicit',
	'I' => 'no-implicit'
};

while ( <$RegFILE> ) {

	if( $mode -eq "" ) {
		if( $line =~ /^\tpublic static final RegistryObject/ ) {
			$mode = "REG_OBJECT";
		}
		next;
	}

	if( $mode -eq "REG_OBJECT") {
		if ( $line =~ /^\h*"([a-zA-Z0-9_]*)"\h*\/\/;!(.*)$/ ) {
			$item_id = $1;
			$args = $2;
			str_getopt(
			generateModels($item_id, $args);
		}
	}
}

close($RegFile);

#Begin by looking into ../../src/main/java/<path>/init/InitItems.java

#An entry begins with /^\h*public static final RegistryObject.

#2nd line of the entry is dedicated to item name. Take that name, and scan the line after it, looking for arguments
#argument pattern is //.*$. If null, generate the item model

#An entry ends with /^\t);/


#Once we are done with the registry file, check ./tmp/ihda-block-items.slof

sub generateModels { # ARGV: item_id, String preparedStatement
	

	#proceed to generation

}
