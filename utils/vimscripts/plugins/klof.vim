function LoadSlof ()
	syntax match Comment /^;.*$/
	syntax region Macro start=/^;!/ end=/$/
	highlight Macro ctermfg=red

endfunction

function LoadKlof()
	call LoadSlof()

	"Syntax
	syntax region Array start=/=\[$/ms=s+1 end=/^]$/

	syntax region Number_Key start=/_\d\+/ms=s+1 end=/[=_]/me=e-1
	syntax region String_Key start=/_[a-zA-Z][a-zA-Z0-9]*/ms=s+1 end=/[=_]/me=e-1
	syntax region Lof_Key start=/^LOF_/ end=/=/me=e-1 contains=Number_Key,String_Key

	syntax region Key start=/^[a-zA-Z][a-zA-Z0-9]*/ end=/=/me=e-1 contains=Number_Key,String_Key,Lof_Key

	syntax region String start=/=[^\[]/ms=s+1 end=/$/

	"Highlights
	highlight Array ctermfg=green

	highlight String_Key ctermfg=yellow
	highlight Number_Key ctermfg=green
	highlight Lof_Key ctermfg=LightRed

	highlight Key ctermfg=yellow

	highlight String ctermfg=magenta

endfunction

au BufRead,BufNewFile *.slof set filetype=slof | call LoadSlof()
au BufRead,BufNewFile *.klof set filetype=klof | call LoadKlof()
