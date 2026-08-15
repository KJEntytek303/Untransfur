#!/usr/bin/perl

use strict;
use warnings;
use FindBin ( qw($RealBin) );
use lib ( $RealBin );
use kgetopt qw(:default);

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

my $init_file = $MODPATH . "/init/InitItems.java";

open( my $RegFILE, "<", $init_file ) or die "Cannot open file $init_file: $!";

my $mode = "";

my $validator_hash_definition = {
	"layers" => '^\d$',
	"custom" => '',
};

my $short_hash_definition = {
	"l" => 'layers',
	'c' => 'custom',
};

mkdir "generated";
mkdir "generated/assets";
mkdir "generated/assets/$MODID/";
mkdir "generated/assets/$MODID/models";
mkdir "generated/assets/$MODID/models/item";

while ( my $line = <$RegFILE> ) {

	if( $mode eq "" ) {
		if( $line =~ /^\tpublic static final RegistryObject/ ) {
			$mode = "REG_OBJECT";
		}
		next;
	}

	if( $mode eq "REG_OBJECT") {
		if ( $line =~ /^\h*"([a-z0-9_]*)",\h*(\/\/;!(.*))?$/ ) {

			$item_id = $1;
			$args = defined $2 ? $3 : "";

			generateModels($item_id, $args);
		}
	}
}

close($RegFILE);

open(my $ihda_bFILE, "<", "tmp/ihda-bi.slof") or exit 0;

while( my $line = <$ihda_bFILE>) {

	if ( $line =~ /^([a-z0-9_]+)(\h(.*)*)?$/ ) {

			$item_id = $1;
			$args = defined $2 ? $3 : "";

			generateBlockItemModels($item_id, $args);
	}
}


#Once we are done with the registry file, check ./tmp/ihda-block-items.slof

sub generateModels { # ARGV: item_id, String preparedStatement{{{
	my $opts = str_getopt($validator_hash_definition, $_[1], $short_hash_definition);
	my $txt_loc = "$MODID:item/$_[0]";
	my $layers = 1;

	if( exists $opts->{"custom"} ) {
		return;
	}

	if( exists $opts->{"layers"} ) {
		$layers = $opts->{"layers"};
	}

	open WFILE, ">", "generated/assets/$MODID/models/item/$_[0].json";
	print WFILE '{
	"parent": "item/generated",
	"textures": {
		';

	print WFILE "\"layer0\": \"$txt_loc\"" . (( $layers > 1 ) ? "," : "");

	for( my $i=1; $i<$layers; $i++ ) {
		print WFILE "\t\t\"layer$i\": \"$MODID:item/$_[0]" . ( $i + 1) . '\"' . (($i<$layers-1) ? "," : "") ;
	}

	print WFILE "\n\t}\n}";
	close WFILE;
}# }}}

sub generateBlockItemModels { # ARGV: id, opts{{{
	my $opts = str_getopt($validator_hash_definition, $_[1], $short_hash_definition);

	if( exists $opts->{"custom"} ) {
		return;
	}

	open WFILE, ">", "generated/assets/$MODID/models/item/$_[0].json";
	print WFILE "{\n\t\"parent\": \"$MODID:$_[0]\"\n}";
	close WFILE;
}# }}}
