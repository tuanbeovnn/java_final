package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddCon implements Initializable {
	private final String machinefile = "application/Name_machine.txt";
	private final String saverPath = "src/application/saver.dat";
	private final String temp_list_path = "temp_list.dat";
	private final HashMap<String, Double> machineList = new HashMap<String, Double>();
	private  HashMap<Date, HashMap<String, Double>> list = new HashMap<Date, HashMap<String, Double>>();
	private final FileChooser filechoose = new FileChooser();
	private File file;
	
	
	
	
	
	
	public AddCon(File file) {
		super();
		this.file = file;
	}


	public void clearSaver() throws IOException {
		

		
		PrintWriter pw = new PrintWriter(saverPath);
		
		pw.close();
		
		
		
		
	}	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	
			
			try {
				
				clearSaver();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error when read Product");
			}
		}
	
	
	
	
	public void WriteFileToSaver(File file) {
		System.out.println("=======Now Start Write Current File in Saver=============");
		try (ObjectOutputStream file_out
	             = new ObjectOutputStream(new FileOutputStream(saverPath))){
	            file_out.writeObject(file);
	        }
	        catch(Exception e) {
	            System.out.println("Problems with save file to saver.dat " + saverPath);
//	            e.printStackTrace();
	        }
		System.out.println("=======Now End Write Current File in Saver=============");
	}
	
	
	@FXML
    private Button saveFile;
	
	
	@FXML
    private void SaveFile(ActionEvent event) throws IOException {
		
		if (this.file == null) {
			System.out.println("Save with no chosen file");
			Stage stage = new Stage();
			this.file = filechoose.showSaveDialog(stage);
	        if (this.file == null) {
	        	
	        	Stage stage2 = new Stage();
	    		Parent root = FXMLLoader.load(getClass().getResource("Alert.fxml"));

	    		stage2.setTitle("Consumption Management");
	    		stage2.setScene(new Scene(root));
	    		stage2.show();
//	        	
	        }
			
		}
		
        SaveToFile(this.file);
		
		
		
    }
	public void SaveToFile(File file) {
		
		try (ObjectOutputStream file_out
	             = new ObjectOutputStream(new FileOutputStream(file))){
			 
			file_out.writeObject(this.list);

	        }
	        catch(Exception e) {
	            System.out.println("Problems with SaveToFile " + file);
	            e.printStackTrace();
	        }
	}
	
	
	
	
	@FXML
    private ComboBox<String> NameOfMachine ;

    @FXML
    private TextField Hours;

    @FXML
    private DatePicker DatePicker;

    @FXML
    private Button AddButton;

    @FXML
    private BarChart<String, Number> DateChart;

    @FXML
    private CategoryAxis ChartX;

    @FXML
    private NumberAxis ChartY;
    @FXML
    private Button closeButton;
    
    
    
    
    	

    

    public void SaveListToTempList() {
    	try (ObjectOutputStream file_out
                = new ObjectOutputStream(new FileOutputStream(temp_list_path))){
               file_out.writeObject(this.list);
           }
           catch(Exception e) {
               System.out.println("Problems with SaveListToTempList" );
               
           }
    }
    
	
	public HashMap<Date, HashMap<String, Double>> readFromTempList(){
		try (ObjectInputStream file_in 
	            = new ObjectInputStream(new FileInputStream(temp_list_path))){
	            return (HashMap<Date, HashMap<String, Double>>)file_in.readObject();
	        }
	        catch(Exception e) {
	            System.out.println("Problems with readFromTempList" );
	            
	        }
		return null;
	}
	
	public void LoadData(ActionEvent event) throws  IOException{
		System.out.println("=======Now in Load Data in Add Consumption=============");
		
		list = readFromTempList();
		System.out.println("Before:");
		
		System.out.println("==========");
		DateChart.getData().clear();
		
		String machine_name = NameOfMachine.getValue();
		String hours = Hours.getText();
		LocalDate date = DatePicker.getValue();
		Date date1 = java.sql.Date.valueOf(date);
		
		
		HashMap<String, Double> temp = list.getOrDefault(date1, new HashMap<String, Double>());
		double temp_hours = temp.getOrDefault(machine_name, 0.0) + Double.valueOf(hours);
		list.put(date1, temp);
		list.get(date1).put(machine_name, temp_hours);
		
		
		
		
		
		String date_format = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		// Get Data to chart
		ChartX.setLabel("Machine Categories used on "+ date_format);
		ChartY.setLabel("kW");
		
		XYChart.Series<String, Number> set1 = new XYChart.Series<>();
		ChartX.setAnimated(false);

		list.get(date1).entrySet().forEach(entry->{
		    
		    set1.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));
		 });
		
		DateChart.getData().addAll(set1);
		System.out.println("After");
		SaveListToTempList();
		System.out.println("=========");
		System.out.println("=======Now End Load Data in Add Consumption=============");
	}
	public void readProducts() throws IOException{
		ClassLoader cl = this.getClass().getClassLoader();
		URL url = cl.getResource(machinefile); 
		

		try(InputStream in = url.openStream(); BufferedReader input = new BufferedReader(new InputStreamReader(in))){
			ArrayList<String> lines = new ArrayList<String>();
			String temp;
			int i = 0;
			while((temp = input.readLine()) != null) {
				lines.add(temp);
				if (i % 2 == 1) {
					machineList.put(lines.get(i-1), Double.parseDouble(lines.get(i)));
				}
				i ++;
				
			}
			List<String> names = machineList.keySet().stream().sorted().collect(Collectors.toList());
			ObservableList<String> options = FXCollections.observableArrayList(names);
			
			NameOfMachine.setItems(FXCollections.observableArrayList(names));
			
			
		}
	}
	
	
	@FXML
	private void closeButtonAction(){
		
	    // get a handle to the stage
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}

	
	
}
