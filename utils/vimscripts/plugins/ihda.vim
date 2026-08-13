function LoadIHDa ()
	syntax region IHDa_macro start=/\/\/;!/ms=s+2 end=/$/
	syntax match javaLineComment +//.*+ contains=@javaCommentSpecial2,javaTodo,javaCommentMarkupTag,javaSpaceError,@Spell,IHDa_macro

	highlight IHDa_macro ctermfg=red
	hi def link javaLineComment Comment
endfunction

au BufRead,BufNewFile *init/*.java call LoadIHDa()
