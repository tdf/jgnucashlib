transaction.setDescription("TODO: AN ERROR HAPPENED BEFORE WE COULD SET THE DESCRIPTION");

print("DEBUG: text=" + text + "\n");


//var invoiceNumber = "" + text;
//invoiceNumber = invoiceNumber.replace(/\n/g, " ");
//invoiceNumber = invoiceNumber.replace(
//                 /.*RG-NR.([0-9][0-9][0-9][0-9][0-9][0-9]-[0-9]*).*/gi,
//                       '$1')

transaction.setDescription("my first script did this");
transaction.setTransactionNumber("");
                       
var NNSplit = transaction.createWritingSplit(file.getAccountByName("other expenses"));
var negatedValue = transaction.getNegatedBalance()
NNSplit.setValue(negatedValue);
NNSplit.setQuantity(negatedValue);
NNSplit.setDescription(text);