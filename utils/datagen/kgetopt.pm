package kgetopt;

use strict;
use warnings;
use Exporter qw (import);

our @EXPORT_OK = qw ( str_getopt $errno );

our %EXPORT_TAGS = ( "default" => \@EXPORT_OK );

our $errno="";

sub getShortOpt {
	#  if noSuchOption in hash - throw
	#  if good - put stuff into ret;
}

sub getLongOpt { # hashRef longopts, string option
	$_[1] =~ /^--([^=]*)=(.*)$/ ;
	my $key = $1;
	my $value = $2;

	if( exists $_[0]->{$key} ) {

		unless( $value =~ /$_[0]->{$key}/ ) {
			$errno = "Value mismatched regex " . $_[0]->{$key} . "\n";
			return {};
		}

		my $ret = { $key => $value };
		return $ret;
	}

	$errno = "Option $key not found in longopt definition";
	return {};
}

#returns a hash_ref to selected opts.
sub str_getopt { #Arglist: getoptHashRef, string, getopt_shortHashRef


	#should be static assert
	if( @_ != 2 && @_ != 3 ) {
		print STDERR "Error: \@_'s lenght is incorrect";
		$errno = "Incorrect argv length";
		return ();
	}

	my @opts = split( ' ', $_[1] );
	my $ret = { "_rest" => [] };

	foreach( @opts ) {

		if( $_ eq "--" ) {
			return $ret;
		}

#		if ( $_ =~ /^-[^-].*/ ) {
#			if( scalar( @_ ) != 3 ) {
#				$errno = "Short options without definition";
#				return {};
#			}
#
#			my $tmp = getShortOpt($_[0], $_[1], $_[2]);
#			if( $errno ne "" ) {
#				return {};
#			}
#			merge_hashes($ret, $tmp)
#			next;
#		}

		if ( $_ =~ /^--[^=]*=.*$/ ) {
			my $tmp = getLongOpt( $_[0], $_[1] );
			if( $errno ne "") {
				return {};
			}
			merge_hashes($ret, $tmp);
			next;
		}

		push( @{$ret->{"_rest"} }, $_ );
	}
	
	return $ret;
}



sub merge_hashes { #argv: returned_hash_ref, added_hash_ref
	foreach ( keys %{$_[1]} ) {
		$_[0]->{$_} = $_[1]->{$_};
	}
}

sub compare_simple_arrays {
	if( @{$_[0]} != @{$_[1]} ) {
		return 0;
	}

	my $i=0;
	foreach( @$_[0] ) {
		if ( ${$_[0]}[$i] ne ${$_[1]}[$i] ) {
			return 0;
		}
		$i++;
	}
	return 1;
}

1;
