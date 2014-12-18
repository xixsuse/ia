package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ComponentiTable extends BaseColumns
	{
		String TABLE_NAME = "componenti";
	 
		String NOME = "nome";
		String CLASSE = "classe";
		String TIPO = "tipo";
		String STATO = "stato";
		
		String[] COLUMNS = new String[] {
				_ID,
				NOME,
				CLASSE,
				TIPO,
				STATO
		};
	
}
