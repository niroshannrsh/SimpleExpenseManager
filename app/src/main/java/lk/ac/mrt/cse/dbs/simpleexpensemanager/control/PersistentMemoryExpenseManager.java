package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by niroshan on 12/4/15.
 */
public class PersistentMemoryExpenseManager extends ExpenseManager {

    public  Context context;
    public PersistentMemoryExpenseManager() {

        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for persistent implementation ***/

        PersistentMemoryTransactionDAO persistentMemoryTransactionDAO=new PersistentMemoryTransactionDAO(context);
        setTransactionsDAO(persistentMemoryTransactionDAO);


        PersistentMemoryAccountDAO persistentMemoryAccountDAO= new PersistentMemoryAccountDAO(context);
        setAccountsDAO(persistentMemoryAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }
}
