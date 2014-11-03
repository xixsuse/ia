package ai.smarthome.database;

import java.text.MessageFormat;

import ai.smarthome.database.table.AccessiTable;
import ai.smarthome.database.table.ConnessioneVeloceTable;
import ai.smarthome.database.table.UtentiTable;
import ai.smarthome.database.wrapper.Utente;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "SmartHome.db";
 	private static final int SCHEMA_VERSION = 1;			// versione del database DA CAMBIARE AD OGNI MODIFICA
 	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sqlUtentiTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} TEXT NOT NULL, {3} TEXT NOT NULL, {4} TEXT NOT NULL, {5} TEXT NOT NULL, {6} TEXT NOT NULL);";
		String sqlAccessiTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} INTEGER NOT NULL, {3} TEXT NOT NULL, FOREIGN KEY({2}) REFERENCES {4}({5}));";
		String sqlUConnessioneVeloceTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} TEXT NOT NULL, {3} TEXT NOT NULL);";
		
		db.execSQL(MessageFormat.format(sqlUtentiTable, UtentiTable.TABLE_NAME, UtentiTable._ID, UtentiTable.USERNAME, UtentiTable.PASSWORD, UtentiTable.COGNOME, UtentiTable.NOME, UtentiTable.MAIL));
		db.execSQL(MessageFormat.format(sqlAccessiTable, AccessiTable.TABLE_NAME, AccessiTable._ID, AccessiTable.USERNAME, AccessiTable.ACCESSO, UtentiTable.TABLE_NAME, UtentiTable._ID));
		db.execSQL(MessageFormat.format(sqlUConnessioneVeloceTable, ConnessioneVeloceTable.TABLE_NAME, ConnessioneVeloceTable._ID, ConnessioneVeloceTable.USERNAME, ConnessioneVeloceTable.PASSWORD));
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	public void setUtente(String username, String password, String cognome, String nome, String mail) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(UtentiTable.USERNAME, username);
		value.put(UtentiTable.PASSWORD, password);
		value.put(UtentiTable.COGNOME, cognome);
		value.put(UtentiTable.NOME, nome);
		value.put(UtentiTable.MAIL, mail);
		db.insert(UtentiTable.TABLE_NAME, null, value);
		
	}
	
	public void updateUtente(String username, String password, String mail) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(UtentiTable.PASSWORD, password);
		value.put(UtentiTable.MAIL, mail);
		db.update(UtentiTable.TABLE_NAME, value, UtentiTable.USERNAME+" = \""+ username + "\"", null);
		
	}		
	public void setConnessioneVeloce(String username, String password) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(ConnessioneVeloceTable.USERNAME, username);
		value.put(ConnessioneVeloceTable.PASSWORD, password);
		
		db.delete(ConnessioneVeloceTable.TABLE_NAME, null, null);
		db.insert(ConnessioneVeloceTable.TABLE_NAME, null, value);
		
	}
	
	
	public void deleteConnessioneVeloce() {
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(ConnessioneVeloceTable.TABLE_NAME, null, null);
		
	}
	
	
	public Utente getConnessioneVeloce() {
		
		Cursor cursore = getReadableDatabase().query(
			ConnessioneVeloceTable.TABLE_NAME, 					// nome della tabella
			ConnessioneVeloceTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			null,										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			ConnessioneVeloceTable.USERNAME);						// ordinamento da applicare ai dati
			
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) {
				return isUtenteRegistered(cursore.getString(1), cursore.getString(2));
			}
			
		return null;
	}
	
	public Cursor geListatUtenti() {
		
		return (getReadableDatabase().query(
			UtentiTable.TABLE_NAME, 					// nome della tabella
			UtentiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			null,										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			UtentiTable.USERNAME));						// ordinamento da applicare ai dati
		
		
	}
	
	public Utente isUtenteRegistered(String username, String password) {
		
		Cursor cursore = getReadableDatabase().query(
			UtentiTable.TABLE_NAME, 					// nome della tabella
			UtentiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			UtentiTable.USERNAME+" = \""+ username + "\" AND " + UtentiTable.PASSWORD +" = \""+ password +"\"",										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			null);						// ordinamento da applicare ai dati
		
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) {
				return (new Utente(cursore.getString(0), cursore.getString(1), cursore.getString(2), cursore.getString(3), cursore.getString(4), cursore.getString(5)));
			}
		
		return null;
	}
	
	public Utente sendMailToUtenteRegistered(String username, String mail) {
		
		Cursor cursore = getReadableDatabase().query(
			UtentiTable.TABLE_NAME, 					// nome della tabella
			UtentiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			UtentiTable.USERNAME+" = \""+ username + "\" AND " + UtentiTable.MAIL +" = \""+ mail +"\"",										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			null);						// ordinamento da applicare ai dati
		
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) {
				return (new Utente(cursore.getString(0), cursore.getString(1), cursore.getString(2), cursore.getString(3), cursore.getString(4), cursore.getString(5)));
			}
		
		return null;
	}
	
	public boolean isUsernameRegistered(String username) {
		
		Cursor cursore = getReadableDatabase().query(
			UtentiTable.TABLE_NAME, 					// nome della tabella
			UtentiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			UtentiTable.USERNAME+" = \""+ username +"\"",										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			UtentiTable.USERNAME);						// ordinamento da applicare ai dati
		
		if (cursore.getCount() > 0)
			return true;
		else
			return false;
	}
	
	public boolean isMailRegistered(String mail) {
		
		Cursor cursore = getReadableDatabase().query(
			UtentiTable.TABLE_NAME, 					// nome della tabella
			UtentiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			UtentiTable.MAIL+" = \""+ mail +"\"",										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			UtentiTable.USERNAME);						// ordinamento da applicare ai dati
		
		if (cursore.getCount() > 0)
			return true;
		else
			return false;
	}

	public void printLogUtenti() {
		Cursor cursore = this.geListatUtenti();
		
		try {
    		while (cursore.moveToNext()) 
    			Log.d("SHE_UtentiTable", cursore.getLong(0) + " " + cursore.getString(1) + "\t " + cursore.getString(2)+ "\t " + cursore.getString(3)+ "\t " + cursore.getString(4)+ "\t " + cursore.getString(5));
    	}  
    	finally {
    		cursore.close();
    	}
	}
	
	
	
	public void setAccesso(String username, String accesso) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(AccessiTable.USERNAME, username);
		value.put(AccessiTable.ACCESSO, accesso);
		db.insert(AccessiTable.TABLE_NAME, null, value);
		
	}
	
	public Cursor getListaAccessi() {
		
		return (getReadableDatabase().query(
			AccessiTable.TABLE_NAME, 					// nome della tabella
			AccessiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			null,										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			AccessiTable.ACCESSO));						// ordinamento da applicare ai dati
	}
	
	public void printLogAccessi() {
		Cursor cursore = this.getListaAccessi();	
		
		try {
    		while (cursore.moveToNext())
    			Log.d("SHE_AccessiTable", cursore.getLong(0) + " " + cursore.getString(1) + "\t " + cursore.getString(2));
    	}  
    	finally {
    		cursore.close();
    	}
	}
	
}