
biz.wolschon.finance.jgnucash.editor.main
is the main-plugin = main-program of the
jGnucashEditor.
It uses
biz.wolschon.finance.jgnucash.viewer.main
wich is ment to run standalone with no
knowledge of the plugin-system.

It contains all the code for writing to
Gnucash-files and can be extended by other plugins.

See plugin.xml for extension-points offered.

Modifications to gnucash files are handled such
that ordering and whitespace are preserved.
Thus loading and writing a file with no modification
is guaranteed to produce a byte-exact copy of the
original file and modifications can be easily inspected
by gunzip+diff.