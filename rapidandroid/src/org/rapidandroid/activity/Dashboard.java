/**
 * 
 */
package org.rapidandroid.activity;

import org.rapidandroid.ActivityConstants;
import org.rapidandroid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * @author Dan Myung (dmyung@dimagi.com)
 * @created 1/9/2009
 */
public class Dashboard extends Activity {
	private String dialogMessage = "";
	
	private boolean mFormSelected = false;
	private int mSelectedFormId = -1;
	private int mMessageSelected = -1;
	
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_VIEW=2;	//this and ACTIVITY_REPORTS don't really need to be reported back to this view.
    private static final int ACTIVITY_REPORTS=3;	    
        	
	private static final int MENU_CREATE_ID = Menu.FIRST;
    private static final int MENU_EDIT_ID = Menu.FIRST + 1;
    private static final int MENU_VIEW_ID = Menu.FIRST + 2;
    private static final int MENU_SHOW_REPORTS = Menu.FIRST + 3;
    //private static final int MENU_EXIT = Menu.FIRST + 3; 	//waitaminute, we don't want to exit this thing, do we?
    
    private static final int CONTEXT_ITEM_TEST1 = ContextMenu.FIRST;
    private static final int CONTEXT_ITEM_TEST2 = ContextMenu.FIRST + 1;
	
    
    protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		this.GetForms();	
		
		
		//Set the event listeners for the spinner and the listview
		Spinner spin_forms = (Spinner) findViewById(R.id.cbx_forms);
		spin_forms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		    {	
				public void onItemSelected(AdapterView<?> parent, View theview, int position, long rowid) {
					//get the position, then reset the 
					mFormSelected = true;
					mSelectedFormId = position;
					GetMessagesForForm(mForms[position]);
				}
	
				public void onNothingSelected(AdapterView<?> parent) {
					// blow away the listview's items
					mFormSelected = false;
					mSelectedFormId = -1;
					GetMessagesForForm("");			
				}
		});	
		
		//add some events to the listview
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
		
		//bind a context menu
		lsv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() { 
		    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, CONTEXT_ITEM_TEST1, 0, "Context 1");				
				menu.add(0, CONTEXT_ITEM_TEST2, 0, "Context 2");
			} 
		  }); 
//		//bind the click event
//		lsv.setOnItemClickListener(new AdapterView.OnItemClickListener()
//	    {
//			public void onItemClick(AdapterView<?> parent, View theparent, int position, long rowid) {
//				// TODO Auto-generated method stub
//				mMessageSelected = position;				
//			}
//	    });
		
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		Bundle extras = null;
		if(intent != null) {
			extras = intent.getExtras();	//right now this is a case where we don't do much activity back and forth
		}
		
        switch(requestCode) {
        case ACTIVITY_CREATE:
            //we should do an update of the view
        	dialogMessage = "Activity Done";
        	showDialog(11);
            break;
        case ACTIVITY_EDIT:
        	dialogMessage = "Activity Done";
        	showDialog(12);            
            break;
        case ACTIVITY_VIEW:
        	dialogMessage = "Activity Done";
        	showDialog(13);            
            break;
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//add images:
		//http://developerlife.com/tutorials/?p=304
		super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_CREATE_ID,0, R.string.dashboard_menu_create);
        menu.add(0, MENU_EDIT_ID,0, R.string.dashboard_menu_edit);
        menu.add(0, MENU_VIEW_ID,0, R.string.dashboard_menu_view);
        menu.add(0, MENU_SHOW_REPORTS,0, R.string.dashboard_menu_show_reports);
        return true;
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
		case MENU_CREATE_ID:
			//showDialog(MENU_CREATE_ID); //debug, we'll need to spawn the activities after this
			StartFormEditActivity(true);
			return true;
		case MENU_EDIT_ID:
			//showDialog(MENU_EDIT_ID); //debug, we'll need to spawn the activities after this
			StartFormEditActivity(false);
			return true;
		case MENU_VIEW_ID:
			//showDialog(MENU_VIEW_ID);	//debug, we'll need to spawn the activities after this
			if(mSelectedFormId != -1) {
				StartFormViewerActivity(this.mForms[mSelectedFormId]);
			} else {
				showDialog(9999);
			}			
			return true;
		case MENU_SHOW_REPORTS:
			//showDialog(MENU_VIEW_ID);	//debug, we'll need to spawn the activities after this
			this.dialogMessage = "TODO:  Go to the reports activity";
			showDialog(9999);					
			
			return true;		
		}
		
		return true;
	}
	
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Flip the enabled status of menu items depending on selection of a form
		super.onPrepareOptionsMenu(menu);		
		
		MenuItem editMenu = menu.findItem(MENU_EDIT_ID);
		editMenu.setEnabled(mFormSelected);
		
		MenuItem viewMenu = menu.findItem(MENU_VIEW_ID);
		viewMenu.setEnabled(mFormSelected);		
		
		MenuItem reportsMenu = menu.findItem(MENU_SHOW_REPORTS);
		reportsMenu.setEnabled(mFormSelected);
		
		return true;		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {		
		super.onCreateDialog(id);
		
		return new AlertDialog.Builder(Dashboard.this)
        .setTitle("Menu Selection")
        .setMessage("Selected Menu Item: " + id + " " + dialogMessage)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        /* User clicked OK so do some stuff */
                    }
                })
        .create();
	}

	
	//Start the form edit/create activity
	private void StartFormEditActivity(boolean isNew) {
		Intent i = new Intent(this, FormEditorActivity.class);
		if(isNew) {
			i.putExtra(ActivityConstants.EDIT_FORM,"");
			startActivityForResult(i, ACTIVITY_CREATE);
		} else {
			i.putExtra(ActivityConstants.EDIT_FORM,mForms[mSelectedFormId]);
			startActivityForResult(i, ACTIVITY_EDIT);
		}
	}
	
	@Override
	//http://www.anddev.org/tinytutcontextmenu_for_listview-t4019.html
	//UGH, things changed from .9 to 1.0
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) { 
		  case CONTEXT_ITEM_TEST1:	
		    // This is actually where the magic happens. 
		    // As we use an adapter view (which the ListView is) 
		    // We can cast item.getMenuInfo() to AdapterContextMenuInfo		    
			   

			  //To get the id of the clicked item in the list use menuInfo.id 
			  dialogMessage = "Context 1: List pos: " + menuInfo.position+ " id:" + menuInfo.id + " mMessageSelected: " + mMessageSelected;
			  showDialog(55);
		    break; 
		  case CONTEXT_ITEM_TEST2:	
			    // This is actually where the magic happens. 
			    // As we use an adapter view (which the ListView is) 
			    // We can cast item.getMenuInfo() to AdapterContextMenuInfo
				  //To get the id of the clicked item in the list use menuInfo.id 
				  dialogMessage = "Context 2: List pos: " + menuInfo.position+ " id:" + menuInfo.id + " mMessageSelected: " + mMessageSelected;
				  showDialog(56);
			    break; 
		  default: 
		    return super.onContextItemSelected(item); 
		  } 
		  return true; 
	}

	private boolean applyMessageContextMenu(MenuItem item) {
		showDialog(item.getItemId());		
		return true;
	}
	
	private void StartFormViewerActivity(String selectedFormName) {
		Intent i = new Intent(this, FormReview.class);
        i.putExtra("FormName", selectedFormName);	//bad form, should use some enum here        
        startActivityForResult(i, ACTIVITY_VIEW);		
	}


	//This is a call to the DB to get all the forms that this form can support.
	private void GetForms() {		
		//The steps: 
		//get the spinner control from the layouts
		Spinner spin_forms = (Spinner) findViewById(R.id.cbx_forms);
		//Get an array of forms from the DB
		//in the current iteration, it's mForms
		
		//Bind that array to an adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mForms);	
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//apply it to the spinner						
		spin_forms.setAdapter(adapter);
		
	}
	
	//this is a call to the DB to update the ListView with the messages for a selected form
	private void GetMessagesForForm(String formname) {
		//the steps:
		//get a hold of the layout resource for the ListView		
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
		
		//Then get the array of the messages for the given form
		//in this case, we're just pulling it from mStrings
		
		//finally, apply it to the view
		//MessageViewAdapter msgAdapter = new MessageViewAdapter(lsv.getContext(), R.layout.message_view);
		
		//lsv.createContextMenu(menu)
		
		if(formname == "") {
			lsv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"Select a form"}));
		} else {
			int start = 0;
			int end = 0;
			if(formname == "Abbaye de Belloc") {
				start = 0;
				end = 129;
			}
			else if(formname == "Abbaye du Mont des Cats") {
				start = 130;
				end = 259;
			}
			else if(formname == "Abertam") {
				start = 260;
				end = 389;	
			}
			else if(formname == "Abondance") {
				start = 390;
				end = 519;
			}
			else if(formname == "Ackawi") {
				start = 520;
				end = 649;
			}
			String[] result = new String[130];
			int q = 0;
			for(int i = start; i < end; i++) {
				result [q++] = mStrings[i];
			}			
			lsv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result));	
		}		
	}
	
	private String[] mForms = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi" ,"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi" ,"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi" ,"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi" 
	};
	
	//654 items
	private String[] mStrings = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
            "Aisy Cendre", "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese",
            "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro", "Appenzell",
            "Aragon", "Ardi Gasna", "Ardrahan", "Armenian String", "Aromes au Gene de Marc",
            "Asadero", "Asiago", "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss",
            "Babybel", "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal", "Banon",
            "Barry's Bay Cheddar", "Basing", "Basket Cheese", "Bath Cheese", "Bavarian Bergkase",
            "Baylough", "Beaufort", "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",
            "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir", "Bierkase", "Bishop Kennedy",
            "Blarney", "Bleu d'Auvergne", "Bleu de Gex", "Bleu de Laqueuille",
            "Bleu de Septmoncel", "Bleu Des Causses", "Blue", "Blue Castello", "Blue Rathgore",
            "Blue Vein (Australian)", "Blue Vein Cheeses", "Bocconcini", "Bocconcini (Australian)",
            "Boeren Leidenkaas", "Bonchester", "Bosworth", "Bougon", "Boule Du Roves",
            "Boulette d'Avesnes", "Boursault", "Boursin", "Bouyssou", "Bra", "Braudostur",
            "Breakfast Cheese", "Brebis du Lavort", "Brebis du Lochois", "Brebis du Puyfaucon",
            "Bresse Bleu", "Brick", "Brie", "Brie de Meaux", "Brie de Melun", "Brillat-Savarin",
            "Brin", "Brin d' Amour", "Brin d'Amour", "Brinza (Burduf Brinza)",
            "Briquette de Brebis", "Briquette du Forez", "Broccio", "Broccio Demi-Affine",
            "Brousse du Rove", "Bruder Basil", "Brusselae Kaas (Fromage de Bruxelles)", "Bryndza",
            "Buchette d'Anjou", "Buffalo", "Burgos", "Butte", "Butterkase", "Button (Innes)",
            "Buxton Blue", "Cabecou", "Caboc", "Cabrales", "Cachaille", "Caciocavallo", "Caciotta",
            "Caerphilly", "Cairnsmore", "Calenzana", "Cambazola", "Camembert de Normandie",
            "Canadian Cheddar", "Canestrato", "Cantal", "Caprice des Dieux", "Capricorn Goat",
            "Capriole Banon", "Carre de l'Est", "Casciotta di Urbino", "Cashel Blue", "Castellano",
            "Castelleno", "Castelmagno", "Castelo Branco", "Castigliano", "Cathelain",
            "Celtic Promise", "Cendre d'Olivet", "Cerney", "Chabichou", "Chabichou du Poitou",
            "Chabis de Gatine", "Chaource", "Charolais", "Chaumes", "Cheddar",
            "Cheddar Clothbound", "Cheshire", "Chevres", "Chevrotin des Aravis", "Chontaleno",
            "Civray", "Coeur de Camembert au Calvados", "Coeur de Chevre", "Colby", "Cold Pack",
            "Comte", "Coolea", "Cooleney", "Coquetdale", "Corleggy", "Cornish Pepper",
            "Cotherstone", "Cotija", "Cottage Cheese", "Cottage Cheese (Australian)",
            "Cougar Gold", "Coulommiers", "Coverdale", "Crayeux de Roncq", "Cream Cheese",
            "Cream Havarti", "Crema Agria", "Crema Mexicana", "Creme Fraiche", "Crescenza",
            "Croghan", "Crottin de Chavignol", "Crottin du Chavignol", "Crowdie", "Crowley",
            "Cuajada", "Curd", "Cure Nantais", "Curworthy", "Cwmtawe Pecorino",
            "Cypress Grove Chevre", "Danablu (Danish Blue)", "Danbo", "Danish Fontina",
            "Daralagjazsky", "Dauphin", "Delice des Fiouves", "Denhany Dorset Drum", "Derby",
            "Dessertnyj Belyj", "Devon Blue", "Devon Garland", "Dolcelatte", "Doolin",
            "Doppelrhamstufel", "Dorset Blue Vinney", "Double Gloucester", "Double Worcester",
            "Dreux a la Feuille", "Dry Jack", "Duddleswell", "Dunbarra", "Dunlop", "Dunsyre Blue",
            "Duroblando", "Durrus", "Dutch Mimolette (Commissiekaas)", "Edam", "Edelpilz",
            "Emental Grand Cru", "Emlett", "Emmental", "Epoisses de Bourgogne", "Esbareich",
            "Esrom", "Etorki", "Evansdale Farmhouse Brie", "Evora De L'Alentejo", "Exmoor Blue",
            "Explorateur", "Feta", "Feta (Australian)", "Figue", "Filetta", "Fin-de-Siecle",
            "Finlandia Swiss", "Finn", "Fiore Sardo", "Fleur du Maquis", "Flor de Guia",
            "Flower Marie", "Folded", "Folded cheese with mint", "Fondant de Brebis",
            "Fontainebleau", "Fontal", "Fontina Val d'Aosta", "Formaggio di capra", "Fougerus",
            "Four Herb Gouda", "Fourme d' Ambert", "Fourme de Haute Loire", "Fourme de Montbrison",
            "Fresh Jack", "Fresh Mozzarella", "Fresh Ricotta", "Fresh Truffles", "Fribourgeois",
            "Friesekaas", "Friesian", "Friesla", "Frinault", "Fromage a Raclette", "Fromage Corse",
            "Fromage de Montagne de Savoie", "Fromage Frais", "Fruit Cream Cheese",
            "Frying Cheese", "Fynbo", "Gabriel", "Galette du Paludier", "Galette Lyonnaise",
            "Galloway Goat's Milk Gems", "Gammelost", "Gaperon a l'Ail", "Garrotxa", "Gastanberra",
            "Geitost", "Gippsland Blue", "Gjetost", "Gloucester", "Golden Cross", "Gorgonzola",
            "Gornyaltajski", "Gospel Green", "Gouda", "Goutu", "Gowrie", "Grabetto", "Graddost",
            "Grafton Village Cheddar", "Grana", "Grana Padano", "Grand Vatel",
            "Grataron d' Areches", "Gratte-Paille", "Graviera", "Greuilh", "Greve",
            "Gris de Lille", "Gruyere", "Gubbeen", "Guerbigny", "Halloumi",
            "Halloumy (Australian)", "Haloumi-Style Cheese", "Harbourne Blue", "Havarti",
            "Heidi Gruyere", "Hereford Hop", "Herrgardsost", "Herriot Farmhouse", "Herve",
            "Hipi Iti", "Hubbardston Blue Cow", "Hushallsost", "Iberico", "Idaho Goatster",
            "Idiazabal", "Il Boschetto al Tartufo", "Ile d'Yeu", "Isle of Mull", "Jarlsberg",
            "Jermi Tortes", "Jibneh Arabieh", "Jindi Brie", "Jubilee Blue", "Juustoleipa",
            "Kadchgall", "Kaseri", "Kashta", "Kefalotyri", "Kenafa", "Kernhem", "Kervella Affine",
            "Kikorangi", "King Island Cape Wickham Brie", "King River Gold", "Klosterkaese",
            "Knockalara", "Kugelkase", "L'Aveyronnais", "L'Ecir de l'Aubrac", "La Taupiniere",
            "La Vache Qui Rit", "Laguiole", "Lairobell", "Lajta", "Lanark Blue", "Lancashire",
            "Langres", "Lappi", "Laruns", "Lavistown", "Le Brin", "Le Fium Orbo", "Le Lacandou",
            "Le Roule", "Leafield", "Lebbene", "Leerdammer", "Leicester", "Leyden", "Limburger",
            "Lincolnshire Poacher", "Lingot Saint Bousquet d'Orb", "Liptauer", "Little Rydings",
            "Livarot", "Llanboidy", "Llanglofan Farmhouse", "Loch Arthur Farmhouse",
            "Loddiswell Avondale", "Longhorn", "Lou Palou", "Lou Pevre", "Lyonnais", "Maasdam",
            "Macconais", "Mahoe Aged Gouda", "Mahon", "Malvern", "Mamirolle", "Manchego",
            "Manouri", "Manur", "Marble Cheddar", "Marbled Cheeses", "Maredsous", "Margotin",
            "Maribo", "Maroilles", "Mascares", "Mascarpone", "Mascarpone (Australian)",
            "Mascarpone Torta", "Matocq", "Maytag Blue", "Meira", "Menallack Farmhouse",
            "Menonita", "Meredith Blue", "Mesost", "Metton (Cancoillotte)", "Meyer Vintage Gouda",
            "Mihalic Peynir", "Milleens", "Mimolette", "Mine-Gabhar", "Mini Baby Bells", "Mixte",
            "Molbo", "Monastery Cheeses", "Mondseer", "Mont D'or Lyonnais", "Montasio",
            "Monterey Jack", "Monterey Jack Dry", "Morbier", "Morbier Cru de Montagne",
            "Mothais a la Feuille", "Mozzarella", "Mozzarella (Australian)",
            "Mozzarella di Bufala", "Mozzarella Fresh, in water", "Mozzarella Rolls", "Munster",
            "Murol", "Mycella", "Myzithra", "Naboulsi", "Nantais", "Neufchatel",
            "Neufchatel (Australian)", "Niolo", "Nokkelost", "Northumberland", "Oaxaca",
            "Olde York", "Olivet au Foin", "Olivet Bleu", "Olivet Cendre",
            "Orkney Extra Mature Cheddar", "Orla", "Oschtjepka", "Ossau Fermier", "Ossau-Iraty",
            "Oszczypek", "Oxford Blue", "P'tit Berrichon", "Palet de Babligny", "Paneer", "Panela",
            "Pannerone", "Pant ys Gawn", "Parmesan (Parmigiano)", "Parmigiano Reggiano",
            "Pas de l'Escalette", "Passendale", "Pasteurized Processed", "Pate de Fromage",
            "Patefine Fort", "Pave d'Affinois", "Pave d'Auge", "Pave de Chirac", "Pave du Berry",
            "Pecorino", "Pecorino in Walnut Leaves", "Pecorino Romano", "Peekskill Pyramid",
            "Pelardon des Cevennes", "Pelardon des Corbieres", "Penamellera", "Penbryn",
            "Pencarreg", "Perail de Brebis", "Petit Morin", "Petit Pardou", "Petit-Suisse",
            "Picodon de Chevre", "Picos de Europa", "Piora", "Pithtviers au Foin",
            "Plateau de Herve", "Plymouth Cheese", "Podhalanski", "Poivre d'Ane", "Polkolbin",
            "Pont l'Eveque", "Port Nicholson", "Port-Salut", "Postel", "Pouligny-Saint-Pierre",
            "Pourly", "Prastost", "Pressato", "Prince-Jean", "Processed Cheddar", "Provolone",
            "Provolone (Australian)", "Pyengana Cheddar", "Pyramide", "Quark",
            "Quark (Australian)", "Quartirolo Lombardo", "Quatre-Vents", "Quercy Petit",
            "Queso Blanco", "Queso Blanco con Frutas --Pina y Mango", "Queso de Murcia",
            "Queso del Montsec", "Queso del Tietar", "Queso Fresco", "Queso Fresco (Adobera)",
            "Queso Iberico", "Queso Jalapeno", "Queso Majorero", "Queso Media Luna",
            "Queso Para Frier", "Queso Quesadilla", "Rabacal", "Raclette", "Ragusano", "Raschera",
            "Reblochon", "Red Leicester", "Regal de la Dombes", "Reggianito", "Remedou",
            "Requeson", "Richelieu", "Ricotta", "Ricotta (Australian)", "Ricotta Salata", "Ridder",
            "Rigotte", "Rocamadour", "Rollot", "Romano", "Romans Part Dieu", "Roncal", "Roquefort",
            "Roule", "Rouleau De Beaulieu", "Royalp Tilsit", "Rubens", "Rustinu", "Saaland Pfarr",
            "Saanenkaese", "Saga", "Sage Derby", "Sainte Maure", "Saint-Marcellin",
            "Saint-Nectaire", "Saint-Paulin", "Salers", "Samso", "San Simon", "Sancerre",
            "Sap Sago", "Sardo", "Sardo Egyptian", "Sbrinz", "Scamorza", "Schabzieger", "Schloss",
            "Selles sur Cher", "Selva", "Serat", "Seriously Strong Cheddar", "Serra da Estrela",
            "Sharpam", "Shelburne Cheddar", "Shropshire Blue", "Siraz", "Sirene", "Smoked Gouda",
            "Somerset Brie", "Sonoma Jack", "Sottocenare al Tartufo", "Soumaintrain",
            "Sourire Lozerien", "Spenwood", "Sraffordshire Organic", "St. Agur Blue Cheese",
            "Stilton", "Stinking Bishop", "String", "Sussex Slipcote", "Sveciaost", "Swaledale",
            "Sweet Style Swiss", "Swiss", "Syrian (Armenian String)", "Tala", "Taleggio", "Tamie",
            "Tasmania Highland Chevre Log", "Taupiniere", "Teifi", "Telemea", "Testouri",
            "Tete de Moine", "Tetilla", "Texas Goat Cheese", "Tibet", "Tillamook Cheddar",
            "Tilsit", "Timboon Brie", "Toma", "Tomme Brulee", "Tomme d'Abondance",
            "Tomme de Chevre", "Tomme de Romans", "Tomme de Savoie", "Tomme des Chouans", "Tommes",
            "Torta del Casar", "Toscanello", "Touree de L'Aubier", "Tourmalet",
            "Trappe (Veritable)", "Trois Cornes De Vendee", "Tronchon", "Trou du Cru", "Truffe",
            "Tupi", "Turunmaa", "Tymsboro", "Tyn Grug", "Tyning", "Ubriaco", "Ulloa",
            "Vacherin-Fribourgeois", "Valencay", "Vasterbottenost", "Venaco", "Vendomois",
            "Vieux Corse", "Vignotte", "Vulscombe", "Waimata Farmhouse Blue",
            "Washed Rind Cheese (Australian)", "Waterloo", "Weichkaese", "Wellington",
            "Wensleydale", "White Stilton", "Whitestone Farmhouse", "Wigmore", "Woodside Cabecou",
            "Xanadu", "Xynotyro", "Yarg Cornish", "Yarra Valley Pyramid", "Yorkshire Blue",
            "Zamorano", "Zanetti Grana Padano", "Zanetti Parmigiano Reggiano"};

}
