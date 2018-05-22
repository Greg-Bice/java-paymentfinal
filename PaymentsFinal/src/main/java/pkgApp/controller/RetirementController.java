
package pkgApp.controller;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import javax.validation.Path.Node;
import org.apache.poi.ss.formula.functions.FinanceLib;
import com.sun.prism.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import javafx.scene.control.Label;
import javafx.geometry.Bounds;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.beans.value.*;
import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {
	
	private RetirementApp mainApp = null;
	
	@FXML
	private TextField txtSaveEachMonth;
	
	@FXML
	private TextField txtYearsToWork;
	
	@FXML
	private TextField txtAnnualReturnWorking;
	
	@FXML
	private TextField txtWhatYouNeedToSave;
	
	@FXML
	private TextField txtYearsRetired;
	
	@FXML
	private TextField txtAnnualReturnRetired;
	
	@FXML
	private TextField txtRequiredIncome;
	
	@FXML
	private TextField txtMonthlySSI;
	
	@FXML
	private BorderPane mainPane;
	
	@FXML
	private Label lblSaveEachMonth, lblYearsToWork, lblWorkAnnualReturn;
	
	@FXML
	private Label lblAmountNeedSave, lblYearsRetired, lblRetiredAnnualReturn, lblRequiredIncome, lblMonthlySSI;
	
	@FXML
	private Button btnCalc;
	
	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();
	private HashMap<TextField, Label> hmLabelList = new HashMap<TextField, Label>();
	
	public RetirementApp getMainApp() {
		
		return mainApp;
	}
	
	public void setMainApp( RetirementApp mainApp ) {
		
		this.mainApp = mainApp;
	}
	
	@Override
	public void initialize( URL location, ResourceBundle resources ) {
		
		hmLabelList.put( txtYearsToWork, lblYearsToWork );
		hmLabelList.put( txtAnnualReturnWorking, lblWorkAnnualReturn );
		hmLabelList.put( txtAnnualReturnRetired, lblRetiredAnnualReturn );
		hmLabelList.put( txtYearsRetired, lblYearsRetired );
		hmLabelList.put( txtRequiredIncome, lblRequiredIncome );
		hmLabelList.put( txtMonthlySSI, lblMonthlySSI );
		
		// Adding an entry in the hashmap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal with an optional floating values.
		
		hmTextFieldRegEx.put( txtYearsToWork, "\\d*?" );
		hmTextFieldRegEx.put( txtAnnualReturnWorking, "\\d*(\\.\\d*)?" );
		hmTextFieldRegEx.put( txtAnnualReturnRetired, "\\d*(\\.\\d*)?" );
		hmTextFieldRegEx.put( txtYearsRetired, "\\d*?" );
		hmTextFieldRegEx.put( txtRequiredIncome, "\\d*?" );
		hmTextFieldRegEx.put( txtMonthlySSI, "\\d*?" );
		
		txtYearsToWork.setTooltip( new Tooltip( "Should be an integer between 0 and 40. (Inclusive)" ) );
		txtAnnualReturnWorking.setTooltip( new Tooltip( "Should be a number between 0% and 10%. (Inclusive)" ) );
		txtAnnualReturnRetired.setTooltip( new Tooltip( "Should be a number between 0% and 10%. (Inclusive)" ) );
		txtYearsRetired.setTooltip( new Tooltip( "Should be an integer between 0 and 20. (Inclusive)" ) );
		txtRequiredIncome.setTooltip( new Tooltip( "Should be an integer between $2,642 and $10,000. (Inclusive)" ) );
		txtRequiredIncome.setTooltip( new Tooltip( "Should be an integer between $0.00 and $2,642. (Inclusive)" ) );
		
		btnCalc.setTooltip( new Tooltip( "Entries required for all fields." ) );
		
		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while ( it.hasNext() ) {
			
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();
			Label lblCompany = hmLabelList.get( txtField );
			
			txtField.focusedProperty().addListener( new ChangeListener<Boolean>() {
				
				@Override
				public void changed( ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue ) {
					
					lblCompany.setTextFill( javafx.scene.paint.Color.web( "#FFFFFF" ) );
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if ( !newPropertyValue ) {
						if ( !txtField.getText().matches( strRegEx ) ) {
							
							lblCompany.setTextFill( javafx.scene.paint.Color.web( "#ea8685" ) );
							txtField.getTooltip().show( txtField, txtField.getScene().getWindow().getX() + txtField.getLayoutX(), txtField.getScene().getWindow().getY() + txtField.getLayoutY() );
							
						}
					}
				}
			} );
		}
	}
	
	@FXML
	public void btnClear( ActionEvent event ) {
		
		// disable read-only controls
		txtSaveEachMonth.setDisable( true );
		txtWhatYouNeedToSave.setDisable( true );
		
		txtSaveEachMonth.setText( "" );
		txtWhatYouNeedToSave.setText( "" );
		
		Iterator it = hmLabelList.entrySet().iterator();
		while ( it.hasNext() ) {
			
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			
			
			txtField.clear();
			txtField.setDisable( false );
			
		}
		
	}
	
	@FXML
	public void btnCalculate() {
		
		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while ( it.hasNext() ) {
			
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			
			if (txtField.getText().isEmpty() ) { 
				btnCalc.getTooltip().show( btnCalc, btnCalc.getScene().getWindow().getX() + btnCalc.getLayoutX(), btnCalc.getScene().getWindow().getY() + btnCalc.getLayoutY() );
				return;
			}
			
		}
		
		txtSaveEachMonth.setDisable( false );
		txtWhatYouNeedToSave.setDisable( false );
		
		Retirement ret = new Retirement( Integer.parseInt( txtYearsToWork.getText() ), Double.parseDouble( txtAnnualReturnWorking.getText() ), Integer.parseInt( txtYearsRetired.getText() ),
				Double.parseDouble( txtAnnualReturnRetired.getText() ), Double.parseDouble( txtRequiredIncome.getText() ), Double.parseDouble( txtMonthlySSI.getText() ) );
		
		txtSaveEachMonth.setText( "$" + NumberFormat.getInstance().format( ret.MonthlySavings() ) );
		txtWhatYouNeedToSave.setText( "$" + NumberFormat.getInstance().format( ret.TotalAmountToSave() ) );
	}

}
