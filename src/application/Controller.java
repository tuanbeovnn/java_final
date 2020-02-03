package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.sun.javafx.collections.MappingChange.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Controller implements Initializable{
	
	private final String machinefile = "application/Name_machine.txt";
	private final String saverPath = "src/application/saver.dat";
	private final String temp_list_path = "temp_list.dat";
	private final HashMap<String, Double> machineList = new HashMap<String, Double>();
	private  HashMap<Date, HashMap<String, Double>> list = new HashMap<Date, HashMap<String, Double>>();

	public void PrintWhatInSaver() {
		System.out.println("This is in Saver: "+GetCurrentFile().getPath());
		
	}
	
	public void PrintList() {
		if (list.isEmpty()) {
			System.out.println("List null");
		}else {
			
			list.entrySet().forEach(entry->{
			    System.out.println( "Ngay "+entry.getKey().toString()); 
			    
			    list.get(entry.getKey()).entrySet().forEach(entry1->{
	    		    System.out.println(entry1.getKey() + " "+ entry1.getValue()); 
	    		    
	    		    
	    		 });
			 });
		}
	}
	
	
	@FXML
    void ExitHome(ActionEvent event) {
		// get a handle to the stage
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	    try {
			System.out.println(" ClearSaver in init!");
			clearSaver();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Error when clear Saver");
		}
    }
	public HashMap<Date, HashMap<String, Double>> getListInit(File file) {
//		File file = GetCurrentFile();
		if (file != null) {
			try (ObjectInputStream file_in 
		            = new ObjectInputStream(new FileInputStream(file.getPath()))){
		            list = (HashMap<Date, HashMap<String, Double>>)file_in.readObject();
		            
		            
		        }
		        catch(Exception e) {
		            System.out.println("No File chosen  " + file.getPath());
//		            e.printStackTrace();
		            return null;
		        }
		}
		return list;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("=======Now Start initialize =============");
		
		if (NameOfMachine != null || statisticChart != null) {
			if (NameOfMachine != null) {
			try {
				System.out.println("readProduct in init!");
				readProducts();

			} catch (IOException e) {
				
				System.out.println("Error when read Product");
			}
			}
		}else {
			
			try {
				System.out.println(" ClearSaver in init!");
				clearSaver();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error when clear Saver");
			}
		}
		System.out.println("=======Now End initialize =============");
	}
	
	@FXML
    private Button openFile;
	
	private final FileChooser filechoose = new FileChooser();
	public void clearSaver() throws IOException {
		

		
		PrintWriter pw = new PrintWriter(saverPath);
		
		pw.close();
		
		
		
		
	}
	public File GetCurrentFile() {
		System.out.println("=======Now Start read Current File in Saver=============");
		try (ObjectInputStream file_in 
	            = new ObjectInputStream(new FileInputStream(saverPath))){
	            File file = (File)file_in.readObject();
	            System.out.println("=======Now End read Current File in Saver=============");
	            return file;
	        }
	        catch(Exception e) {
	            System.out.println("No chosen File " );

	            
	        }
		System.out.println("=======Now End read Current File in Saver=============");
		return null;
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
    private void OpenFile(ActionEvent event) throws IOException {
		
		System.out.println("=======Now Start Open File=============");
        Stage stage = new Stage();
        File file = filechoose.showOpenDialog(stage);
        if (file != null) {
        	
        	WriteFileToSaver(file);
        	
        }
        System.out.println("This is Open File: " );
        PrintWhatInSaver();
        System.out.println("=======Now End Open File=============");
        
		
		
    }
	
	@FXML
    private Button saveFile;
	
	
	@FXML
    private void SaveFile(ActionEvent event) throws IOException {
		File file = GetCurrentFile();
		if (file == null) {
			System.out.println("Save with no chosen file");
			Stage stage = new Stage();
			file = filechoose.showSaveDialog(stage);
	        if (file == null) {
	        	
	        	Stage stage2 = new Stage();
	    		Parent root = FXMLLoader.load(getClass().getResource("Alert.fxml"));

	    		stage2.setTitle("Consumption Management");
	    		stage2.setScene(new Scene(root));
	    		stage2.show();
//	        	
	        }
			
		}
		
        SaveToFile(file);
		
		
		
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
    
	@FXML
    private void AddConsumption(ActionEvent event) throws IOException {
		
        Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("AddConsumption.fxml"));

		stage.setTitle("Consumption Management");
		stage.setScene(new Scene(root));
		stage.show();
		
		File file = GetCurrentFile();
		list = getListInit(file);
		PrintList();
		SaveListToTempList();
		
		
		
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
		System.out.println(AddButton);
		readProducts();
		list = readFromTempList();
		System.out.println("Before:");
		PrintList();
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
		this.machineList.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + " " +entry.getValue());
		});

		list.get(date1).entrySet().forEach(entry->{
		    
		    set1.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue() * this.machineList.get(entry.getKey())  ));
		 });
		
		DateChart.getData().addAll(set1);
		System.out.println("After");
		PrintList();
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
			try {
				NameOfMachine.setItems(FXCollections.observableArrayList(names));
			} catch (Exception e) {
				
			}
			
			
			
		}
	}
	
	
	@FXML
	private void closeButtonAction(){
		
	    // get a handle to the stage
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
	 

	
	    
	
	
	
	
	
	@FXML
    private BarChart<String, Number> statisticChart;

   

    @FXML
    private Label sum;

    @FXML
    private Label max;

    @FXML
    private Label mean;

    @FXML
    private Label median;
	
    @FXML
    private Button drawBarChart;
    
    @FXML
    private CategoryAxis ChartXStatistic;
    
    @FXML
    private Label nameOfFile;

	

	@FXML
	public void Statistic(ActionEvent event) throws IOException {
		System.out.println("=======Now Start Statistic=============");
		File file = GetCurrentFile();
		
		if (file == null) {
			System.out.println("No file chose");
			Stage stage = new Stage();
	        file = filechoose.showOpenDialog(stage);
	        if (file == null) {
	        	Stage stage2 = new Stage();
	    		Parent root = FXMLLoader.load(getClass().getResource("Alert.fxml"));

	    		stage2.setTitle("Consumption Management");
	    		stage2.setScene(new Scene(root));
	    		stage2.show();
	        }
	        
		}
		WriteFileToSaver(file);
		
        
        	
		
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("Satistic.fxml"));

		stage.setTitle("Consumption Management");
		stage.setScene(new Scene(root));
		stage.show();
		
		System.out.println("=======Now End Statistic=============");
		
		
	}
	public void LoadDataStatistic(ActionEvent event) throws  IOException{
		
		File file = GetCurrentFile();
		this.list = getListInit(file);
		readProducts();
		
		machineList.entrySet().forEach(entry ->{
			
			System.out.println(entry.getKey() + " " + entry.getValue());
		});
		HashMap<String, Double> statistic_hash = new HashMap<String, Double>();
		
		list.entrySet().forEach(entry->{
		    
		    
		    list.get(entry.getKey()).entrySet().forEach(entry1->{
    		    statistic_hash.put(entry1.getKey(), statistic_hash.getOrDefault(entry1.getKey(), 0.0) + entry1.getValue());
    		    
    		    
    		 });
		 });
		
		ArrayList<Double> kw = new ArrayList<Double>();
		
		
		statisticChart.getData().clear();
		
		
		XYChart.Series<String, Number> set1 = new XYChart.Series<>();
		ChartXStatistic.setAnimated(false);
//		set1.getData().add(new XYChart.Data<String, Number>("ABC", 10));
//		set1.getData().add(new XYChart.Data<String, Number>("ascfa", 18));
//		set1.getData().add(new XYChart.Data<String, Number>("asf", 2));
//		set1.getData().add(new XYChart.Data<String, Number>("asf", 5));
//		set1.getData().add(new XYChart.Data<String, Number>("Afa", 7));
//		set1.getData().add(new XYChart.Data<String, Number>("fasd", 11));
//		set1.getData().add(new XYChart.Data<String, Number>("Afas", 13));
		
		statistic_hash.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + " " + entry.getValue());
			set1.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue() * this.machineList.get(entry.getKey())));
			kw.add(entry.getValue() * this.machineList.get(entry.getKey()));
		});
		
		statisticChart.getData().addAll(set1);
		DecimalFormat df = new DecimalFormat("#.##");
		sum.setText("Sum: " + df.format(kw.stream().mapToDouble(x -> x).sum()) );
		max.setText("Max: " + df.format(Collections.max(kw)) );
		mean.setText("Min: " + df.format(Collections.min(kw)));
		median.setText("Mean: " + df.format(kw.stream().mapToDouble(x -> x).sum() / kw.size()));
		
	}
	
	
	
}
