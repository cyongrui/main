//@@author A0132785Y
package doordonote.ui;

import doordonote.common.Task;
import doordonote.logic.Logic;
import doordonote.logic.UIToLogic;
import doordonote.logic.UIState; 

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.lang.StringBuilder;

import org.apache.log4j.BasicConfigurator;
import org.apache.commons.lang.WordUtils;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.TOP_CENTER;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Shape;

/**
 *
 * @author Priyanka
 */
public class UI extends Application {
	
	public enum ListType {
	    DELETED, FINISHED, NORMAL
	}
	
	private static final String STATE_UPDATE = "Update";
	private static final String STATE_DISPLAY = "Display";
	private static final String HELP = "help";
	private static final String HELP_ADD = "add";
	private static final String HELP_DELETE = "delete";
	private static final String HELP_UPDATE = "update";
	private static final String HELP_FIND = "find";
	private static final String HELP_FINISH = "finish";
	private static final String HELP_PATH = "path";
	private static final String HELP_RESTORE = "restore";
	private static final String HELP_GET = "get";
	private static final String STATE_FIND = "Find";
	private static final String STATE_HOME = "Home";
	private static final String STATE_DISPLAY_FINISH = "Display finish";
	private static final String STATE_DISPLAY_DELETE = "Display delete";
    
    Text output = new Text("Welcome to DoOrDoNote!");
    Text title = new Text("Home");
    
    UIToLogic logic = null;
    
    public UI() {
        try {
			logic = new Logic();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    BorderPane border = new BorderPane();
    Scene scene = new Scene(border);
    
    @Override
    public void start(Stage primaryStage) {
        UIState state = new UIState();
        border.setBottom(addVBoxB());
        border.setCenter(addHBox(0, state.getDisplayType()));
        border.setTop(addHeader(state.getDisplayType()));
        
  
        primaryStage.setScene(scene);
        primaryStage.setTitle("DoOrDoNote");
//        primaryStage.getIcons().add(new Image("icon.jpg"));
        primaryStage.show();
        primaryStage.setMaximized(true);
        
        Stage helpStage = createHelpWindow();
        helpStage.show();
        helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
              @Override
              public void handle(KeyEvent evt) {
                if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
                    helpStage.close();
                }
              }
            });
    }
    
    protected VBox addVBoxB() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 12, 10, 12));
        vbox.setSpacing(5);
        vbox.setStyle("-fx-background-color: #383737;");
        
        HBox hb = new HBox();
        hb.setStyle("-fx-background-color: #F0F0F0;"); //E9EFFD #F5F8FF
        hb.setAlignment(CENTER);
        hb.setPadding(new Insets(5, 5, 5, 5));
        output.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
        output.setFill(Color.web("#00811C"));
        hb.getChildren().add(output);
        
        vbox.getChildren().addAll(hb, addHBox2());
        vbox.setAlignment(CENTER);
        return vbox;
        
    }
    protected HBox addHBox2() {
        
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(5, 12, 5, 12));
        hbox.setSpacing(10);
        //hbox.setStyle("-fx-background-color: #336699;");
        
        Label command = new Label("Command:");
        command.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
        command.setTextFill(Color.web("#FFFFFF"));
        TextField commandBox = new TextField();
        commandBox.setPrefWidth(500);
        
        getUserInput(commandBox);  
      
        hbox.getChildren().addAll(command, commandBox);
        hbox.setAlignment(CENTER);
        
        return hbox;
    }
    
    protected void getUserInput(TextField commandBox) {
        
        commandBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
         String feedback;
        @Override
        public void handle(KeyEvent ke)
        {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                if(commandBox.getText() != null) {
                    try {
                    	feedback = logic.parseAndExecuteCommand(commandBox.getText());
                    	/*border.setCenter(addHBox());
                    	output.setText(feedback);   
	                	output.setFill(Color.web("#00811C"));
	                	commandBox.clear();
	                	*/
                    	UIState state = logic.getState();
                    	
                    	if(state.getHelpBox() == null) {
                    		output.setText(feedback);
                    	    output.setFill(Color.web("#00811C"));
                    	    border.setCenter(addHBox(state.getIdNewTask()+1, state.getDisplayType()));
                    	    border.setTop(addHeader(state.getDisplayType()));
                    	    if(state.getTitle() != null) {
                    	        title.setText(state.getTitle());
                    	    }
                    		if(state.getInputBox() == null || state.getInputBox() == "") {
                    			commandBox.clear();
                    		}
                    		else {
                        		commandBox.setText(state.getInputBox());
                        	    commandBox.positionCaret(state.getInputBox().length() + 1);
                    		}
                    	}
                    	else {
                    		output.setText(feedback);
                    	    output.setFill(Color.web("#00811C"));
                    	    
                    	    Stage helpStage;
                    	    switch(state.getHelpBox()) {
                    	    case HELP_ADD: {
                    	    	helpStage = createHelpAddWindow();
                    	    	break;
                    	    }
                    	    case HELP_DELETE: {
                    	    	helpStage = createHelpDeleteWindow();
                    	    	break;
                    	    }
                    	    case HELP_FIND: {
                    	    	helpStage = createHelpFindWindow();
                    	    	break;
                    	    }
                    	    case HELP_FINISH: {
                    	    	helpStage = createHelpFinishWindow();
                    	    	break;
                    	    }
                    	    case HELP_GET: {
                    	    	helpStage = createHelpGetWindow();
                    	    	break;
                    	    }
                    	    case HELP_PATH: {
                    	    	helpStage = createHelpPathWindow();
                    	    	break;
                    	    }
                    	    case HELP_RESTORE: {
                    	    	helpStage = createHelpRestoreWindow();
                    	    	break;
                    	    }
                    	    case HELP_UPDATE: {
                    	    	helpStage = createHelpUpdateWindow();
                    	    	break;
                    	    }
                    	    default: {
                    	    	helpStage = createHelpWindow();
                    	    	break;
                    	    }
                    	    }
                    		
  	                        helpStage.show();
  	                        helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
  	                          @Override
  	                          public void handle(KeyEvent evt) {
  	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
  	                                helpStage.close();
  	                            }
  	                          }
  	                        });
  	                        commandBox.clear();
                    	}

                    	
                    	/*switch(state) {
                    	case STATE_HELP: {
                    	   Stage helpStage = createHelpWindow();
 	                       helpStage.show();
 	                       helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
 	                          @Override
 	                          public void handle(KeyEvent evt) {
 	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
 	                                helpStage.close();
 	                            }
 	                          }
 	                       });
 	                      commandBox.clear();
 	                       break;
                    	}
                    	case STATE_HELP_ADD: {
                    		Stage helpStage = createHelpAddWindow();
  	                       helpStage.show();
  	                       helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
  	                          @Override
  	                          public void handle(KeyEvent evt) {
  	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
  	                                helpStage.close();
  	                            }
  	                          }
  	                       });
  	                     commandBox.clear();
  	                     
  	                       break;
                    	}
                    	case STATE_HELP_DELETE: {
                    		Stage helpStage = createHelpDeleteWindow();
   	                       helpStage.show();
   	                       helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
   	                          @Override
   	                          public void handle(KeyEvent evt) {
   	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
   	                                helpStage.close();
   	                            }
   	                          }
   	                       });
   	                     commandBox.clear();
   	                     
   	                       break;
                    	}
                    	case STATE_HELP_UPDATE: {
                    		Stage helpStage = createHelpUpdateWindow();
    	                       helpStage.show();
    	                       helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
    	                          @Override
    	                          public void handle(KeyEvent evt) {
    	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
    	                                helpStage.close();
    	                            }
    	                          }
    	                       });
    	                     commandBox.clear();
    	                     
    	                       break;
                    	}
                    	case STATE_HELP_FIND: {
                    		Stage helpStage = createHelpFindWindow();
 	                       helpStage.show();
 	                       helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
 	                          @Override
 	                          public void handle(KeyEvent evt) {
 	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
 	                                helpStage.close();
 	                            }
 	                          }
 	                       });
 	                     commandBox.clear();
 	                     
 	                       break;
                    	}
                    	case STATE_HELP_FINISH: {
                    		Stage helpStage = createHelpFinishWindow();
  	                       helpStage.show();
  	                       helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
  	                          @Override
  	                          public void handle(KeyEvent evt) {
  	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
  	                                helpStage.close();
  	                            }
  	                          }
  	                       });
  	                     commandBox.clear();
  	                     
  	                       break;
                    	}
                    	case STATE_HELP_PATH: {
                    		Stage helpStage = createHelpPathWindow();
   	                       helpStage.show();
   	                       helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
   	                          @Override
   	                          public void handle(KeyEvent evt) {
   	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
   	                                helpStage.close();
   	                            }
   	                          }
   	                       });
   	                     commandBox.clear();
   	                     
   	                       break;
                    	}
                    	case STATE_HELP_RESTORE: {
                    		Stage helpStage = createHelpRestoreWindow();
    	                       helpStage.show();
    	                       helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
    	                          @Override
    	                          public void handle(KeyEvent evt) {
    	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
    	                                helpStage.close();
    	                            }
    	                          }
    	                       });
    	                     commandBox.clear();
    	                     
    	                       break;
                    	}
                    	case STATE_HELP_GET: {
                    		Stage helpStage = createHelpGetWindow();
 	                       helpStage.show();
 	                       helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
 	                          @Override
 	                          public void handle(KeyEvent evt) {
 	                            if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
 	                                helpStage.close();
 	                            }
 	                          }
 	                       });
 	                     commandBox.clear();
 	                     
 	                       break;
                    	}
                    	case STATE_DISPLAY_FINISH: {
                    		border.setCenter(addHBox());
                    		border.setTop(addCompleteHeader());
		                    commandBox.clear();
		                    output.setText("");
		                    break;
                    	}
                    	case STATE_DISPLAY_DELETE: {
                    		border.setCenter(addHBox());
                    		border.setTop(addDeleteHeader());
		                    commandBox.clear();
		                    output.setText("");
		                    break;
                    	}
                    	case STATE_FIND: {
                    		border.setCenter(addHBox());
                    		border.setTop(addFindHeader());
		                    commandBox.clear();
		                    output.setText(feedback);
		                    break;         		
                    	}
                    	case STATE_DISPLAY: {
                    		output.setText(feedback);   
		                	output.setFill(Color.web("#00811C"));
				            border.setCenter(addHBox());
		                    commandBox.clear();
		                    break;
                    	}
                    	case STATE_HOME: {
                    		output.setText(feedback);   
		                	output.setFill(Color.web("#00811C"));
				            border.setCenter(addHBox());
                    		border.setTop(addHeader());
                    		commandBox.clear();
                    		break;
                    	}
                    	case STATE_UPDATE: {
                    	    commandBox.setText(logic.getTaskToBeUpdated());
                    	    commandBox.positionCaret(logic.getTaskToBeUpdated().length() + 1);
                    	    output.setText(feedback);
                    	    output.setFill(Color.web("#00811C"));
                    	    border.setCenter(addHBox());
                    		border.setTop(addHeader());
                    	    break;
                    	}
                    	} */
                    }
                    catch (Exception e) {
                    	feedback = e.getMessage();
                    	UIState state = new UIState();
                    	border.setCenter(addHBox(0, state.getDisplayType()));
                    	output.setText(feedback);
                    	output.setFill(Color.web("#F20505"));
                    }
		            
                }
                else {
                	UIState state = new UIState();
                	border.setCenter(addHBox(0, state.getDisplayType()));
                	output.setText("");
                }
            }
                
             if (ke.getCode().equals(KeyCode.ESCAPE)) {
                	try {
                		UIState state = logic.getState();
                    	feedback = logic.parseAndExecuteCommand("home");
                    	output.setText(feedback);   
	                	output.setFill(Color.web("#00811C"));
			            border.setCenter(addHBox(0, state.getDisplayType()));
			            border.setTop(addHeader(state.getDisplayType()));
			            if(state.getTitle() != null) {
                	        title.setText(state.getTitle());
                	    }
                		commandBox.clear();
                	}
                	catch (Exception e) {
                    	feedback = e.getMessage();
                    	output.setText(feedback);
                    	output.setFill(Color.web("#F20505"));
                    }
                }
             
             if (ke.getCode().equals(KeyCode.Z) && ke.isControlDown()) {
             	try {
             		UIState state = logic.getState();
                 	feedback = logic.parseAndExecuteCommand("undo");
                 	output.setText(feedback);   
	                	output.setFill(Color.web("#00811C"));
			            border.setCenter(addHBox(0, state.getDisplayType()));
			            border.setTop(addHeader(state.getDisplayType()));
			            if(state.getTitle() != null) {
             	        title.setText(state.getTitle());
             	    }
             		commandBox.clear();
             	}
             	catch (Exception e) {
                 	feedback = e.getMessage();
                 	output.setText(feedback);
                 	output.setFill(Color.web("#F20505"));
                 }
             }
             
             if (ke.getCode().equals(KeyCode.Y) && ke.isControlDown()) {
              	try {
              		UIState state = logic.getState();
                  	feedback = logic.parseAndExecuteCommand("redo");
                  	output.setText(feedback);   
 	                	output.setFill(Color.web("#00811C"));
 			            border.setCenter(addHBox(0, state.getDisplayType()));
 			            border.setTop(addHeader(state.getDisplayType()));
 			            if(state.getTitle() != null) {
              	        title.setText(state.getTitle());
              	    }
              		commandBox.clear();
              	}
              	catch (Exception e) {
                  	feedback = e.getMessage();
                  	output.setText(feedback);
                  	output.setFill(Color.web("#F20505"));
                  }
              }
             
             if (ke.getCode().equals(KeyCode.D) && ke.isControlDown()) {
              	try {
              		UIState state = logic.getState();
                  	feedback = logic.parseAndExecuteCommand("display deleted");
                  	output.setText(feedback);   
 	                	output.setFill(Color.web("#00811C"));
 			            border.setCenter(addHBox(0, state.getDisplayType()));
 			            border.setTop(addHeader(state.getDisplayType()));
 			            if(state.getTitle() != null) {
              	        title.setText(state.getTitle());
              	    }
              		commandBox.clear();
              	}
              	catch (Exception e) {
                  	feedback = e.getMessage();
                  	output.setText(feedback);
                  	output.setFill(Color.web("#F20505"));
                  }
              }
             
             if (ke.getCode().equals(KeyCode.F) && ke.isControlDown()) {
              	try {
              		UIState state = logic.getState();
                  	feedback = logic.parseAndExecuteCommand("display finished");
                  	output.setText(feedback);   
 	                	output.setFill(Color.web("#00811C"));
 			            border.setCenter(addHBox(0, state.getDisplayType()));
 			            border.setTop(addHeader(state.getDisplayType()));
 			            if(state.getTitle() != null) {
              	        title.setText(state.getTitle());
              	    }
              		commandBox.clear();
              	}
              	catch (Exception e) {
                  	feedback = e.getMessage();
                  	output.setText(feedback);
                  	output.setFill(Color.web("#F20505"));
                  }
              }
             
             if (ke.getCode().equals(KeyCode.H) && ke.isControlDown()) {
               	try {
                   	feedback = logic.parseAndExecuteCommand(HELP);
                   	output.setText(feedback);   
  	                output.setFill(Color.web("#00811C"));
  	                Stage helpStage = createHelpWindow();
  	                helpStage.show();
                    helpStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                      @Override
                      public void handle(KeyEvent evt) {
                        if (evt.getCode().equals(KeyCode.ESCAPE)|| evt.getCode().equals(KeyCode.ENTER)) {
                            helpStage.close();
                        }
                      }
                    });

               		commandBox.clear();
               	}
               	catch (Exception e) {
                   	feedback = e.getMessage();
                   	output.setText(feedback);
                   	output.setFill(Color.web("#F20505"));
                   }
               }
             
        }
        });
    }
    
    public Stage createOverdueWindow() {
    	Stage stage = new Stage();
    	
    	
    	stage.setTitle("Overdue!");
        //stage.getIcons().add(image1);
    	return stage;
    }
    
    public Stage createHelpWindow() {
    	Stage stage = new Stage();
    	
    	HBox hb = new HBox();
    	hb.setSpacing(15);
    	hb.setAlignment(CENTER);
        Image image1 = new Image("question_mark.png");
        ImageView imv1 = new ImageView(image1);
        imv1.setFitWidth(30);
        imv1.setPreserveRatio(true);
        imv1.setSmooth(true);
        imv1.setCache(true);
        Text helpHeader = new Text("Hello! This is the page to provide you with all the help you need for DoOrDoNote");
        helpHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        helpHeader.setFill(Color.web("#00143E")); //#00143E
        hb.getChildren().addAll(helpHeader, imv1);
        
        Text tableHeader = new Text("Here is a table of all the commands you can use:");
        tableHeader.setFont(Font.font("Calibri", FontWeight.NORMAL, 17));
        tableHeader.setFill(Color.web("#00143E"));
        
        Image image2 = new Image("help_resize.jpg");
        ImageView imv2 = new ImageView(image2);
        imv2.setFitWidth(700);
        imv2.setPreserveRatio(true);
        imv2.setSmooth(true);
        imv2.setCache(true);
        
        Button bt = new Button(" OK! ");
       
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 30, 10, 30));
        vb.setSpacing(10);
        vb.getChildren().addAll(hb, tableHeader, imv2, bt);
        vb.setAlignment(TOP_CENTER);
        vb.setStyle("-fx-background-color: #eff4ff;");
        Scene sc = new Scene(vb);
        
        stage.setTitle("Help!");
        stage.getIcons().add(image1);
        stage.setScene(sc);
        
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            stage.close();      
            }
        });
        
        return stage;
    	
    }
    
    public Stage createHelpAddWindow() {
    	Stage stage = new Stage();
    	HBox hb = new HBox();
    	hb.setSpacing(15);
    	hb.setAlignment(CENTER);
        Image image1 = new Image("question_mark.png");
        ImageView imv1 = new ImageView(image1);
        imv1.setFitWidth(30);
        imv1.setPreserveRatio(true);
        imv1.setSmooth(true);
        imv1.setCache(true);
        Text helpHeader = new Text("Hello! Here is a table of the commands you can use for ADD:");
        helpHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        helpHeader.setFill(Color.web("#00143E")); //#00143E
        hb.getChildren().addAll(helpHeader, imv1);
        
        Image image2 = new Image("helpadd.jpg");
        ImageView imv2 = new ImageView(image2);
        imv2.setFitWidth(700);
        imv2.setPreserveRatio(true);
        imv2.setSmooth(true);
        imv2.setCache(true);
        
        Button bt = new Button(" OK! ");
       
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 30, 10, 30));
        vb.setSpacing(10);
        vb.getChildren().addAll(hb, imv2, bt);
        vb.setAlignment(TOP_CENTER);
        vb.setStyle("-fx-background-color: #eff4ff;");
        Scene sc = new Scene(vb);
        
        stage.setTitle("Help!");
        stage.getIcons().add(image1);
        stage.setScene(sc);
        
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            stage.close();      
            }
        });
        
        return stage;
    	
    }
    
    public Stage createHelpDeleteWindow() {
    	Stage stage = new Stage();
    	HBox hb = new HBox();
    	hb.setSpacing(15);
    	hb.setAlignment(CENTER);
        Image image1 = new Image("question_mark.png");
        ImageView imv1 = new ImageView(image1);
        imv1.setFitWidth(30);
        imv1.setPreserveRatio(true);
        imv1.setSmooth(true);
        imv1.setCache(true);
        Text helpHeader = new Text("Hello! Here is the command you can use for DELETE:");
        helpHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        helpHeader.setFill(Color.web("#00143E")); //#00143E
        hb.getChildren().addAll(helpHeader, imv1);
        
        Image image2 = new Image("helpdelete.jpg");
        ImageView imv2 = new ImageView(image2);
        imv2.setFitWidth(700);
        imv2.setPreserveRatio(true);
        imv2.setSmooth(true);
        imv2.setCache(true);
        
        Button bt = new Button(" OK! ");
       
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 30, 10, 30));
        vb.setSpacing(10);
        vb.getChildren().addAll(hb, imv2, bt);
        vb.setAlignment(TOP_CENTER);
        vb.setStyle("-fx-background-color: #eff4ff;");
        Scene sc = new Scene(vb);
        
        stage.setTitle("Help!");
        stage.getIcons().add(image1);
        stage.setScene(sc);
        
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            stage.close();      
            }
        });
        
        return stage;
    	
    }
    
    public Stage createHelpUpdateWindow() {
    	Stage stage = new Stage();
    	HBox hb = new HBox();
    	hb.setSpacing(15);
    	hb.setAlignment(CENTER);
        Image image1 = new Image("question_mark.png");
        ImageView imv1 = new ImageView(image1);
        imv1.setFitWidth(30);
        imv1.setPreserveRatio(true);
        imv1.setSmooth(true);
        imv1.setCache(true);
        Text helpHeader = new Text("Hello! Here is the command you can use for UPDATE:");
        helpHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        helpHeader.setFill(Color.web("#00143E")); //#00143E
        hb.getChildren().addAll(helpHeader, imv1);
        
        Image image2 = new Image("helpupdate.jpg");
        ImageView imv2 = new ImageView(image2);
        imv2.setFitWidth(700);
        imv2.setPreserveRatio(true);
        imv2.setSmooth(true);
        imv2.setCache(true);
        
        Button bt = new Button(" OK! ");
       
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 30, 10, 30));
        vb.setSpacing(10);
        vb.getChildren().addAll(hb, imv2, bt);
        vb.setAlignment(TOP_CENTER);
        vb.setStyle("-fx-background-color: #eff4ff;");
        Scene sc = new Scene(vb);
        
        stage.setTitle("Help!");
        stage.getIcons().add(image1);
        stage.setScene(sc);
        
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            stage.close();      
            }
        });
        
        return stage;
    	
    }
    
    public Stage createHelpFindWindow() {
    	Stage stage = new Stage();
    	HBox hb = new HBox();
    	hb.setSpacing(15);
    	hb.setAlignment(CENTER);
        Image image1 = new Image("question_mark.png");
        ImageView imv1 = new ImageView(image1);
        imv1.setFitWidth(30);
        imv1.setPreserveRatio(true);
        imv1.setSmooth(true);
        imv1.setCache(true);
        Text helpHeader = new Text("Hello! Here is a table of the commands you can use for FIND:");
        helpHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        helpHeader.setFill(Color.web("#00143E")); //#00143E
        hb.getChildren().addAll(helpHeader, imv1);
        
        Image image2 = new Image("helpfind.jpg");
        ImageView imv2 = new ImageView(image2);
        imv2.setFitWidth(700);
        imv2.setPreserveRatio(true);
        imv2.setSmooth(true);
        imv2.setCache(true);
        
        Button bt = new Button(" OK! ");
       
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 30, 10, 30));
        vb.setSpacing(10);
        vb.getChildren().addAll(hb, imv2, bt);
        vb.setAlignment(TOP_CENTER);
        vb.setStyle("-fx-background-color: #eff4ff;");
        Scene sc = new Scene(vb);
        
        stage.setTitle("Help!");
        stage.getIcons().add(image1);
        stage.setScene(sc);
        
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            stage.close();      
            }
        });
        
        return stage;
    	
    }
    
    public Stage createHelpFinishWindow() {
    	Stage stage = new Stage();
    	HBox hb = new HBox();
    	hb.setSpacing(15);
    	hb.setAlignment(CENTER);
        Image image1 = new Image("question_mark.png");
        ImageView imv1 = new ImageView(image1);
        imv1.setFitWidth(30);
        imv1.setPreserveRatio(true);
        imv1.setSmooth(true);
        imv1.setCache(true);
        Text helpHeader = new Text("Hello! Here is the command you can use for FINISH:");
        helpHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        helpHeader.setFill(Color.web("#00143E")); //#00143E
        hb.getChildren().addAll(helpHeader, imv1);
        
        Image image2 = new Image("helpfinish.jpg");
        ImageView imv2 = new ImageView(image2);
        imv2.setFitWidth(700);
        imv2.setPreserveRatio(true);
        imv2.setSmooth(true);
        imv2.setCache(true);
        
        Button bt = new Button(" OK! ");
       
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 30, 10, 30));
        vb.setSpacing(10);
        vb.getChildren().addAll(hb, imv2, bt);
        vb.setAlignment(TOP_CENTER);
        vb.setStyle("-fx-background-color: #eff4ff;");
        Scene sc = new Scene(vb);
        
        stage.setTitle("Help!");
        stage.getIcons().add(image1);
        stage.setScene(sc);
        
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            stage.close();      
            }
        });
        
        return stage;
    	
    }
    
    public Stage createHelpPathWindow() {
    	Stage stage = new Stage();
    	HBox hb = new HBox();
    	hb.setSpacing(15);
    	hb.setAlignment(CENTER);
        Image image1 = new Image("question_mark.png");
        ImageView imv1 = new ImageView(image1);
        imv1.setFitWidth(30);
        imv1.setPreserveRatio(true);
        imv1.setSmooth(true);
        imv1.setCache(true);
        Text helpHeader = new Text("Hello! Here the command you can use for PATH:");
        helpHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        helpHeader.setFill(Color.web("#00143E")); //#00143E
        hb.getChildren().addAll(helpHeader, imv1);
        
        Image image2 = new Image("helppath.jpg");
        ImageView imv2 = new ImageView(image2);
        imv2.setFitWidth(700);
        imv2.setPreserveRatio(true);
        imv2.setSmooth(true);
        imv2.setCache(true);
        
        Button bt = new Button(" OK! ");
       
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 30, 10, 30));
        vb.setSpacing(10);
        vb.getChildren().addAll(hb, imv2, bt);
        vb.setAlignment(TOP_CENTER);
        vb.setStyle("-fx-background-color: #eff4ff;");
        Scene sc = new Scene(vb);
        
        stage.setTitle("Help!");
        stage.getIcons().add(image1);
        stage.setScene(sc);
        
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            stage.close();      
            }
        });
        
        return stage;
    	
    }
    
    public Stage createHelpRestoreWindow() {
    	Stage stage = new Stage();
    	HBox hb = new HBox();
    	hb.setSpacing(15);
    	hb.setAlignment(CENTER);
        Image image1 = new Image("question_mark.png");
        ImageView imv1 = new ImageView(image1);
        imv1.setFitWidth(30);
        imv1.setPreserveRatio(true);
        imv1.setSmooth(true);
        imv1.setCache(true);
        Text helpHeader = new Text("Hello! Here is the command you can use for RESTORE:");
        helpHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        helpHeader.setFill(Color.web("#00143E")); //#00143E
        hb.getChildren().addAll(helpHeader, imv1);
        
        Image image2 = new Image("helprestore.jpg");
        ImageView imv2 = new ImageView(image2);
        imv2.setFitWidth(700);
        imv2.setPreserveRatio(true);
        imv2.setSmooth(true);
        imv2.setCache(true);
        
        Button bt = new Button(" OK! ");
       
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 30, 10, 30));
        vb.setSpacing(10);
        vb.getChildren().addAll(hb, imv2, bt);
        vb.setAlignment(TOP_CENTER);
        vb.setStyle("-fx-background-color: #eff4ff;");
        Scene sc = new Scene(vb);
        
        stage.setTitle("Help!");
        stage.getIcons().add(image1);
        stage.setScene(sc);
        
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            stage.close();      
            }
        });
        
        return stage;
    	
    }
    
    public Stage createHelpGetWindow() {
    	Stage stage = new Stage();
    	HBox hb = new HBox();
    	hb.setSpacing(15);
    	hb.setAlignment(CENTER);
        Image image1 = new Image("question_mark.png");
        ImageView imv1 = new ImageView(image1);
        imv1.setFitWidth(30);
        imv1.setPreserveRatio(true);
        imv1.setSmooth(true);
        imv1.setCache(true);
        Text helpHeader = new Text("Hello! Here is the command you can use for GET:");
        helpHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        helpHeader.setFill(Color.web("#00143E")); //#00143E
        hb.getChildren().addAll(helpHeader, imv1);
        
        Image image2 = new Image("helpget.jpg");
        ImageView imv2 = new ImageView(image2);
        imv2.setFitWidth(700);
        imv2.setPreserveRatio(true);
        imv2.setSmooth(true);
        imv2.setCache(true);
        
        Button bt = new Button(" OK! ");
       
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 30, 10, 30));
        vb.setSpacing(10);
        vb.getChildren().addAll(hb, imv2, bt);
        vb.setAlignment(TOP_CENTER);
        vb.setStyle("-fx-background-color: #eff4ff;");
        Scene sc = new Scene(vb);
        
        stage.setTitle("Help!");
        stage.getIcons().add(image1);
        stage.setScene(sc);
        
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            stage.close();      
            }
        });
        
        return stage;
    	
    }
    
    protected HBox addHBox(int n, doordonote.logic.UIState.ListType listType) {
        
        HBox main = new HBox();
        
        main.setPadding(new Insets(40, 25, 30, 25));
        main.setSpacing(40);
        main.setStyle("-fx-background-color: #FFFFFF;");
        
        if(listType.equals(doordonote.logic.UIState.ListType.NORMAL)) {
        	return displayHomeTasks(main, n);
        }
        else {
            return displayDeletedOrFinishedTasks(main);	
        }
    }
    
    protected HBox displayHomeTasks(HBox main, int n) {
        
    	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        List<Task> taskList = logic.getTasks();
        boolean haveEventsOrDeadlines = true;
        boolean haveFloatingTasks = false;
        boolean haveSameDate = true;
        boolean haveEventsSpanningDays = false;
        int count = 1;
        int i, j;
        
        VBox v1 = new VBox();
        v1.setPrefWidth(500);
        v1.setStyle("-fx-background-color: #E1F5EF;");
        
        VBox vbox1 = new VBox();
        vbox1.setAlignment(Pos.TOP_LEFT);
        vbox1.setPadding(new Insets(18, 18, 18, 18));
        vbox1.setSpacing(15);
        vbox1.setPrefWidth(500);
        vbox1.setStyle("-fx-background-color: #E1F5EF;");
        
        ScrollPane sp1 = new ScrollPane();
        VBox.setVgrow(sp1, Priority.ALWAYS);
        //sp1.setVmax(440);
        sp1.setFitToHeight(true);
        sp1.setFitToWidth(true);
        sp1.setPrefSize(115, 150);
        sp1.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp1.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        double scrollPaneIncrement = 0.2;
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evt) {
                if (evt.getCode().equals(KeyCode.UP)) {
                	if (sp1.getVvalue() > sp1.getVmin()) {
                        sp1.setVvalue(sp1.getVvalue() - scrollPaneIncrement);
                    }
                }
                if (evt.getCode().equals(KeyCode.DOWN)) {
                	if (sp1.getVvalue() < sp1.getVmax()) {
                        sp1.setVvalue(sp1.getVvalue() + scrollPaneIncrement);
                    }
                }
            }
        }); 
        
    	VBox v2_1 = new VBox();
        v2_1.setPrefWidth(500);
        v2_1.setStyle("-fx-background-color: #FFF3F3;");
        
        VBox v2_2 = new VBox();
        v2_2.setPrefWidth(500);
        v2_2.setStyle("-fx-background-color: #F9FFC6;");
        
        VBox vbox2 = new VBox();
        vbox2.setAlignment(Pos.TOP_LEFT);
        vbox2.setPadding(new Insets(18, 18, 18, 18));
        vbox2.setSpacing(15);
        vbox2.setPrefWidth(500);
        vbox2.setStyle("-fx-background-color: #F9FFC6;");
        
        ScrollPane sp2 = new ScrollPane();
        VBox.setVgrow(sp2, Priority.ALWAYS);
        //sp2.setVmax(440);
        sp2.setFitToHeight(true);
        sp2.setFitToWidth(true);
        sp2.setPrefSize(115, 150);
        sp2.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp2.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evt) {
                if (evt.getCode().equals(KeyCode.UP)) {
                	if (sp2.getVvalue() > sp2.getVmin()) {
                        sp2.setVvalue(sp2.getVvalue() - scrollPaneIncrement);
                    }
                }
                if (evt.getCode().equals(KeyCode.DOWN)) {
                	if (sp2.getVvalue() < sp2.getVmax()) {
                        sp2.setVvalue(sp2.getVvalue() + scrollPaneIncrement);
                    }
                }
            }
        });
        
        
        for(i = 0; i < taskList.size(); i++) {
            if(!(taskList.get(i).getType().equals("FLOATING_TASK"))) {
            	if(taskList.get(i).getType().equals("EVENT_TASK")) {
            		String startDate = formatter.format(taskList.get(i).getStartDate());
            		String endDate = formatter.format(taskList.get(i).getEndDate());
            		
            		
            		if(!(startDate.equals(endDate))) {
            			haveEventsSpanningDays = true;
            			continue;
            		}
            	}
            	
                Calendar calEnd = DateToCalendar(taskList.get(i).getEndDate());
                String day = getDay(calEnd); 
                String month = getMonth(calEnd);
                int date = calEnd.get(calEnd.DAY_OF_MONTH);
                String timeEnd = getTime(calEnd);
                Text taskDate;
                if(checkForToday(taskList.get(i).getEndDate())) {
                	taskDate = new Text("Today, " + day + ", " + date + " " + month);
                }
                else {
                    taskDate = new Text(day + ", " + date + " " + month);
                }
                taskDate.setFont(Font.font("Calibri", FontWeight.BOLD, 22));
                taskDate.setTextAlignment(TextAlignment.CENTER);
                taskDate.setFill(Color.web("#0C1847"));
                
                HBox dates = new HBox();
                dates.setAlignment(TOP_CENTER);
                dates.getChildren().add(taskDate);
                
                Text taskDesc;
                String task ;
                if(taskList.get(i).getType().equals("DEADLINE_TASK")) {
                	task = count++ + ". " + "[by " + timeEnd + "] " + taskList.get(i).getDescription();
                    taskDesc = new Text(WordUtils.wrap(task, 62, "\n", true));
                    FillTransition colour;
                    if(checkForOverdue(taskList.get(i).getEndDate())) {
                        taskDesc.setFill(Color.RED);
                       	colour = changeColour(taskDesc, Color.RED);
                      }
                     else {
                        colour = changeColour(taskDesc, Color.BLACK);
                     }
                    if(count == n+1) {
                    	 Timeline blinker = createBlinker(taskDesc);
                         SequentialTransition blink = new SequentialTransition(
                                 taskDesc,
                                 blinker
                         );
                         
                         blink.play();
                         colour.play();
                    }
                }
                else {
                    Calendar calStart = DateToCalendar(taskList.get(i).getStartDate());
                    String timeStart = getTime(calStart);
                    task = count++ + ". " + "[" + timeStart + "-" + timeEnd + "] " + taskList.get(i).getDescription();
                	taskDesc = new Text(WordUtils.wrap(task, 62, "\n", true));
                	FillTransition colour;
                	if(checkForOngoing(taskList.get(i).getStartDate(), taskList.get(i).getEndDate())) {
                     	taskDesc.setFill(Color.web("#0F6F00"));
                     	colour = changeColour(taskDesc, Color.web("#0F6F00"));
                     }
                     else if(checkForOverdue(taskList.get(i).getEndDate())) {
                       	taskDesc.setFill(Color.RED);
                       	colour = changeColour(taskDesc, Color.RED);
                     }
                     else {
                    	 colour = changeColour(taskDesc, Color.BLACK);
                     }
                	if(count == n+1) {
                   	 Timeline blinker = createBlinker(taskDesc);
                        SequentialTransition blink = new SequentialTransition(
                                taskDesc,
                                blinker
                        );
                        
                        blink.play();
                        colour.play();
                   }
                }
                
                taskDesc.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
                vbox1.getChildren().addAll(dates, taskDesc);
                for(j = i+1; j < taskList.size(); j++) {
                   haveSameDate = true;
                   if(!(taskList.get(j).getType().equals("FLOATING_TASK"))) {
                   	  if(taskList.get(j).getType().equals("EVENT_TASK")) {
                		String startDate = formatter.format(taskList.get(j).getStartDate());
                		String endDate = formatter.format(taskList.get(j).getEndDate());
                		
                		
                		if(!(startDate.equals(endDate))) {
                			haveEventsSpanningDays = true;
                			continue;
                		}
                	  }
                      Calendar calEnd2 = DateToCalendar(taskList.get(j).getEndDate());
                      String month2 = getMonth(calEnd2);
                      int date2 = calEnd2.get(calEnd2.DAY_OF_MONTH);
                      String timeEnd2 = getTime(calEnd2);
                      if((date != date2)||!(month.equals(month2)))
                          haveSameDate = false;
                      else {
                          Text taskDesc2;
                          FillTransition colour;
                          if(taskList.get(j).getType().equals("DEADLINE_TASK")) {
                        	 task = count++ + ". " + "[by " + timeEnd2 + "] " + taskList.get(j).getDescription();
                             taskDesc2 = new Text(WordUtils.wrap(task, 62, "\n", true));
                             
                             if(checkForOverdue(taskList.get(j).getEndDate())) {
                                taskDesc2.setFill(Color.RED);
                               	 colour = changeColour(taskDesc2, Color.RED);
                              }
                             else {
                                colour = changeColour(taskDesc2, Color.BLACK);
                             }
                          }
                          else {
                             Calendar calStart2 = DateToCalendar(taskList.get(j).getStartDate());
                             String timeStart2 = getTime(calStart2);
                             task = count++ + ". " + "[" + timeStart2 + "-" + timeEnd2 + "] " + taskList.get(j).getDescription();
                             taskDesc2 = new Text(WordUtils.wrap(task, 62, "\n", true));
                             if(checkForOngoing(taskList.get(j).getStartDate(), taskList.get(j).getEndDate())) {
                             	taskDesc2.setFill(Color.web("#0F6F00"));
                             	colour = changeColour(taskDesc2, Color.web("#0F6F00"));
                             }
                             else if(checkForOverdue(taskList.get(j).getEndDate())) {
                               	taskDesc2.setFill(Color.RED);
                               	colour = changeColour(taskDesc2, Color.RED);
                             }
                             else {
                            	 colour = changeColour(taskDesc2, Color.BLACK);
                             }
                          }
                          taskDesc2.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
                          if(count == n+1) {
                            	 Timeline blinker = createBlinker(taskDesc2);                       
                                 SequentialTransition blink = new SequentialTransition(
                                         taskDesc2,
                                         blinker
                                 );
                                 
                                 blink.play();
                                 colour.play();
                            }
                          vbox1.getChildren().addAll(taskDesc2);
                          i++;
                      }
                   }
                   else {
                       haveEventsOrDeadlines = false;
                   }
                   if(haveEventsOrDeadlines == false || haveSameDate == false) {
                       break;
                   }
                }
            }
            else {
                haveEventsOrDeadlines = false;
            }
      
            if(haveEventsOrDeadlines == false) {
                break;
            }

        }
        
        sp1.setContent(vbox1);
        v1.getChildren().addAll(sp1);
        
        if(haveEventsSpanningDays == true) {
            
            VBox vbox3 = new VBox();
            vbox3.setAlignment(Pos.TOP_LEFT);
            vbox3.setPadding(new Insets(18, 18, 18, 18));
            vbox3.setSpacing(15);
            vbox3.setPrefWidth(500);
            vbox3.setStyle("-fx-background-color: #FFF3F3;");
            
            ScrollPane sp3 = new ScrollPane();
            VBox.setVgrow(sp3, Priority.ALWAYS);
            //sp2.setVmax(440);
            sp3.setFitToHeight(true);
            sp3.setFitToWidth(true);
            sp3.setPrefSize(115, 150);
            sp3.setHbarPolicy(ScrollBarPolicy.NEVER);
            sp3.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
            
            scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent evt) {
                    if (evt.getCode().equals(KeyCode.UP)) {
                    	if (sp3.getVvalue() > sp3.getVmin()) {
                            sp3.setVvalue(sp3.getVvalue() - scrollPaneIncrement);
                        }
                    }
                    if (evt.getCode().equals(KeyCode.DOWN)) {
                    	if (sp3.getVvalue() < sp3.getVmax()) {
                            sp3.setVvalue(sp3.getVvalue() + scrollPaneIncrement);
                        }
                    }
                }
            });
            
            HBox events = new HBox();
            events.setAlignment(Pos.TOP_CENTER);
            Text eventsHeader = new Text("Events Spanning Days");
            eventsHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 22));
            eventsHeader.setTextAlignment(TextAlignment.CENTER);
            eventsHeader.setFill(Color.web("#560000"));
            events.getChildren().add(eventsHeader);
            vbox3.getChildren().add(events);
            
            for(i=0; i<taskList.size(); i++) {
            	if(taskList.get(i).getType().equals("EVENT_TASK")) {
                	if(taskList.get(i).getType().equals("EVENT_TASK")) {
                		String start = formatter.format(taskList.get(i).getStartDate());
                		String end = formatter.format(taskList.get(i).getEndDate());
                		
                		
                		if(!(start.equals(end))) {
                			Calendar calStart = DateToCalendar(taskList.get(i).getStartDate());
                			String startDay = getDay(calStart); 
                            String startMonth = getMonth(calStart);
                            String startTime = getTime(calStart);
                            int startDate = calStart.get(calStart.DAY_OF_MONTH);
                            
                            Calendar calEnd = DateToCalendar(taskList.get(i).getEndDate());
                            String endDay = getDay(calEnd); 
                            String endMonth = getMonth(calEnd);
                            String endTime = getTime(calEnd);
                            int endDate = calEnd.get(calEnd.DAY_OF_MONTH);
                            
                			String eventTask = (count++ + ". " + "[" + startDay + ", " + startDate + " " + startMonth + ", " + startTime + " - " + endDay + ", " + endDate + " " + endMonth + ", " + endTime + "] " + taskList.get(i).getDescription());
                            Text eventDisplay = new Text(WordUtils.wrap(eventTask, 62, "\n", true));
                            eventDisplay.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
                            FillTransition colour;
                            if(checkForOverdue(taskList.get(i).getEndDate())) {
                            	eventDisplay.setFill(Color.RED);
                            	colour = changeColour(eventDisplay, Color.RED);
                            }
                            else if(checkForOngoing(taskList.get(i).getStartDate(), taskList.get(i).getEndDate())) {
                            	eventDisplay.setFill(Color.web("#0F6F00"));
                            	colour = changeColour(eventDisplay, Color.web("#0F6F00"));
                            }
                            else {
                            	colour = changeColour(eventDisplay, Color.BLACK);
                            }
                            if(count == n+1) {
                           	 Timeline blinker = createBlinker(eventDisplay);
                           	
                                SequentialTransition blink = new SequentialTransition(
                                        eventDisplay,
                                        blinker
                                );
                                
                                blink.play();
                                colour.play();
                           }
                            vbox3.getChildren().add(eventDisplay);
                		}
                	}
            	}
            	
            }
            
            sp3.setContent(vbox3);
            v2_1.getChildren().addAll(sp3);
        }
        
        HBox floating = new HBox();
        floating.setAlignment(TOP_CENTER);
        Text floatingHeader = new Text("Floating Tasks");
        floatingHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 22));
        floatingHeader.setTextAlignment(TextAlignment.CENTER);
        floatingHeader.setFill(Color.web("#3C220A"));
        floating.getChildren().add(floatingHeader);
        vbox2.getChildren().add(floating);
        
        for(i=0; i<taskList.size(); i++) {
        	if(taskList.get(i).getType().equals("FLOATING_TASK")) {
        		haveFloatingTasks = true;
                        String floatingTask = (count++ + ". " + taskList.get(i).getDescription());

                        Text floatingDisplay = new Text(WordUtils.wrap(floatingTask, 62, "\n", true));
                        floatingDisplay.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
                        if(count == n+1) {
                       	 Timeline blinker = createBlinker(floatingDisplay);
                       	 FillTransition colour = changeColour(floatingDisplay, Color.BLACK);
                            SequentialTransition blink = new SequentialTransition(
                                    floatingDisplay,
                                    blinker
                            );
                            
                            blink.play();
                            colour.play();
                       }
                        vbox2.getChildren().add(floatingDisplay);
        	}
        }

        if(haveFloatingTasks == false) {
            Text noFloatingTasks = new Text("*none*");
            noFloatingTasks.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
            vbox2.getChildren().add(noFloatingTasks);
        }
        
        sp2.setContent(vbox2);
        v2_2.getChildren().addAll(sp2);
        
        if(haveEventsSpanningDays == true) {
        	VBox v2 = new VBox();
            v2.setSpacing(10);
            v2_1.setPrefHeight(235);
            v2_2.setPrefHeight(235);
        	v2.getChildren().addAll(v2_1, v2_2);
        	main.getChildren().addAll(v1, v2);
        }
        else {
        	main.getChildren().addAll(v1, v2_2);
        }
        
        main.setAlignment(TOP_CENTER);
        
        
        return main;
        
    }
    
    protected HBox displayDeletedOrFinishedTasks(HBox main) {
        
    	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        List<Task> taskList = logic.getTasks();
        boolean haveEventsOrDeadlines = true;
        boolean haveFloatingTasks = false;
        boolean haveSameDate = true;
        boolean haveEventsSpanningDays = false;
        int count = 1;
        int i, j;
        
        VBox v1 = new VBox();
        v1.setPrefWidth(500);
        v1.setStyle("-fx-background-color: #E1F5EF;");
        
        VBox vbox1 = new VBox();
        vbox1.setAlignment(TOP_CENTER);
        vbox1.setPadding(new Insets(18, 18, 18, 18));
        vbox1.setSpacing(15);
        vbox1.setPrefWidth(500);
        vbox1.setStyle("-fx-background-color: #E1F5EF;");
        
        ScrollPane sp1 = new ScrollPane();
        VBox.setVgrow(sp1, Priority.ALWAYS);
        //sp1.setVmax(440);
        sp1.setFitToHeight(true);
        sp1.setFitToWidth(true);
        sp1.setPrefSize(115, 150);
        sp1.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp1.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        double scrollPaneIncrement = 0.2;
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evt) {
                if (evt.getCode().equals(KeyCode.UP)) {
                	if (sp1.getVvalue() > sp1.getVmin()) {
                        sp1.setVvalue(sp1.getVvalue() - scrollPaneIncrement);
                    }
                }
                if (evt.getCode().equals(KeyCode.DOWN)) {
                	if (sp1.getVvalue() < sp1.getVmax()) {
                        sp1.setVvalue(sp1.getVvalue() + scrollPaneIncrement);
                    }
                }
            }
        }); 
        
    	VBox v2_1 = new VBox();
        v2_1.setPrefWidth(500);
        v2_1.setStyle("-fx-background-color: #FFF3F3;");
        
        VBox v2_2 = new VBox();
        v2_2.setPrefWidth(500);
        v2_2.setStyle("-fx-background-color: #F9FFC6;");
        
        VBox vbox2 = new VBox();
        vbox2.setAlignment(TOP_CENTER);
        vbox2.setPadding(new Insets(18, 18, 18, 18));
        vbox2.setSpacing(15);
        vbox2.setPrefWidth(500);
        vbox2.setStyle("-fx-background-color: #F9FFC6;");
        
        ScrollPane sp2 = new ScrollPane();
        VBox.setVgrow(sp2, Priority.ALWAYS);
        //sp2.setVmax(440);
        sp2.setFitToHeight(true);
        sp2.setFitToWidth(true);
        sp2.setPrefSize(115, 150);
        sp2.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp2.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evt) {
                if (evt.getCode().equals(KeyCode.UP)) {
                	if (sp2.getVvalue() > sp2.getVmin()) {
                        sp2.setVvalue(sp2.getVvalue() - scrollPaneIncrement);
                    }
                }
                if (evt.getCode().equals(KeyCode.DOWN)) {
                	if (sp2.getVvalue() < sp2.getVmax()) {
                        sp2.setVvalue(sp2.getVvalue() + scrollPaneIncrement);
                    }
                }
            }
        });
        
        for(i = 0; i < taskList.size(); i++) {
            if(!(taskList.get(i).getType().equals("FLOATING_TASK"))) {
            	if(taskList.get(i).getType().equals("EVENT_TASK")) {
            		String startDate = formatter.format(taskList.get(i).getStartDate());
            		String endDate = formatter.format(taskList.get(i).getEndDate());
            		
            		
            		if(!(startDate.equals(endDate))) {
            			haveEventsSpanningDays = true;
            			continue;
            		}
            	}
            	
                Calendar calEnd = DateToCalendar(taskList.get(i).getEndDate());
                String day = getDay(calEnd); 
                String month = getMonth(calEnd);
                int date = calEnd.get(calEnd.DAY_OF_MONTH);
                String timeEnd = getTime(calEnd);
                Text taskDate;
                if(checkForToday(taskList.get(i).getEndDate())) {
                	taskDate = new Text("Today, " + day + ", " + date + " " + month);
                }
                else {
                    taskDate = new Text(day + ", " + date + " " + month);
                }
                taskDate.setFont(Font.font("Calibri", FontWeight.BOLD, 22));
                taskDate.setTextAlignment(TextAlignment.CENTER);
                taskDate.setFill(Color.web("#0C1847"));
                Text taskDesc;
                String task ;
                if(taskList.get(i).getType().equals("DEADLINE_TASK")) {
                	task = count++ + ". " + "[by " + timeEnd + "] " + taskList.get(i).getDescription();
                    taskDesc = new Text(WordUtils.wrap(task, 62, "\n", true));
                }
                else {
                    Calendar calStart = DateToCalendar(taskList.get(i).getStartDate());
                    String timeStart = getTime(calStart);
                    task = count++ + ". " + "[" + timeStart + "-" + timeEnd + "] " + taskList.get(i).getDescription();
                	taskDesc = new Text(WordUtils.wrap(task, 62, "\n", true));
                }
                
                taskDesc.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
                vbox1.getChildren().addAll(taskDate, taskDesc);
                for(j = i+1; j < taskList.size(); j++) {
                   haveSameDate = true;
                   if(!(taskList.get(j).getType().equals("FLOATING_TASK"))) {
                   	  if(taskList.get(j).getType().equals("EVENT_TASK")) {
                		String startDate = formatter.format(taskList.get(j).getStartDate());
                		String endDate = formatter.format(taskList.get(j).getEndDate());
                		
                		
                		if(!(startDate.equals(endDate))) {
                			haveEventsSpanningDays = true;
                			continue;
                		}
                	  }
                      Calendar calEnd2 = DateToCalendar(taskList.get(j).getEndDate());
                      String month2 = getMonth(calEnd2);
                      int date2 = calEnd2.get(calEnd2.DAY_OF_MONTH);
                      String timeEnd2 = getTime(calEnd2);
                      if((date != date2)||!(month.equals(month2)))
                          haveSameDate = false;
                      else {
                          Text taskDesc2;
                          if(taskList.get(j).getType().equals("DEADLINE_TASK")) {
                        	 task = count++ + ". " + "[by " + timeEnd2 + "] " + taskList.get(j).getDescription();
                             taskDesc2 = new Text(WordUtils.wrap(task, 62, "\n", true));
                          }
                          else {
                             Calendar calStart2 = DateToCalendar(taskList.get(j).getStartDate());
                             String timeStart2 = getTime(calStart2);
                             task = count++ + ". " + "[" + timeStart2 + "-" + timeEnd2 + "] " + taskList.get(j).getDescription();
                             taskDesc2 = new Text(WordUtils.wrap(task, 62, "\n", true));
                          }
                          taskDesc2.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
                          vbox1.getChildren().addAll(taskDesc2);
                          i++;
                      }
                   }
                   else {
                       haveEventsOrDeadlines = false;
                   }
                   if(haveEventsOrDeadlines == false || haveSameDate == false) {
                       break;
                   }
                }
            }
            else {
                haveEventsOrDeadlines = false;
            }
      
            if(haveEventsOrDeadlines == false) {
                break;
            }

        }
        
        sp1.setContent(vbox1);
        v1.getChildren().addAll(sp1);
        
        if(haveEventsSpanningDays == true) {
            
            VBox vbox3 = new VBox();
            vbox3.setAlignment(TOP_CENTER);
            vbox3.setPadding(new Insets(18, 18, 18, 18));
            vbox3.setSpacing(15);
            vbox3.setPrefWidth(500);
            vbox3.setStyle("-fx-background-color: #FFF3F3;");
            
            ScrollPane sp3 = new ScrollPane();
            VBox.setVgrow(sp3, Priority.ALWAYS);
            //sp2.setVmax(440);
            sp3.setFitToHeight(true);
            sp3.setFitToWidth(true);
            sp3.setPrefSize(115, 150);
            sp3.setHbarPolicy(ScrollBarPolicy.NEVER);
            sp3.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
            
            scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent evt) {
                    if (evt.getCode().equals(KeyCode.UP)) {
                    	if (sp3.getVvalue() > sp3.getVmin()) {
                            sp3.setVvalue(sp3.getVvalue() - scrollPaneIncrement);
                        }
                    }
                    if (evt.getCode().equals(KeyCode.DOWN)) {
                    	if (sp3.getVvalue() < sp3.getVmax()) {
                            sp3.setVvalue(sp3.getVvalue() + scrollPaneIncrement);
                        }
                    }
                }
            });
            
            Text eventsHeader = new Text("Events Spanning Days");
            eventsHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 22));
            eventsHeader.setTextAlignment(TextAlignment.CENTER);
            eventsHeader.setFill(Color.web("#560000"));
            vbox3.getChildren().add(eventsHeader);
            
            for(i=0; i<taskList.size(); i++) {
            	if(taskList.get(i).getType().equals("EVENT_TASK")) {
                	if(taskList.get(i).getType().equals("EVENT_TASK")) {
                		String start = formatter.format(taskList.get(i).getStartDate());
                		String end = formatter.format(taskList.get(i).getEndDate());
                		
                		
                		if(!(start.equals(end))) {
                			Calendar calStart = DateToCalendar(taskList.get(i).getStartDate());
                			String startDay = getDay(calStart); 
                            String startMonth = getMonth(calStart);
                            String startTime = getTime(calStart);
                            int startDate = calStart.get(calStart.DAY_OF_MONTH);
                            
                            Calendar calEnd = DateToCalendar(taskList.get(i).getEndDate());
                            String endDay = getDay(calEnd); 
                            String endMonth = getMonth(calEnd);
                            String endTime = getTime(calEnd);
                            int endDate = calEnd.get(calEnd.DAY_OF_MONTH);
                            
                			String eventTask = (count++ + ". " + "[" + startDay + ", " + startDate + " " + startMonth + ", " + startTime + " - " + endDay + ", " + endDate + " " + endMonth + ", " + endTime + "] " + taskList.get(i).getDescription());
                            Text eventDisplay = new Text(WordUtils.wrap(eventTask, 62, "\n", true));
                            eventDisplay.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
                            vbox3.getChildren().add(eventDisplay);
                		}
                	}
            	}
            	
            }
            
            sp3.setContent(vbox3);
            v2_1.getChildren().addAll(sp3);
        }
        
        Text floatingHeader = new Text("Floating Tasks");
        floatingHeader.setFont(Font.font("Calibri", FontWeight.BOLD, 22));
        floatingHeader.setTextAlignment(TextAlignment.CENTER);
        floatingHeader.setFill(Color.web("#3C220A"));
        vbox2.getChildren().add(floatingHeader);
        
        for(i=0; i<taskList.size(); i++) {
        	if(taskList.get(i).getType().equals("FLOATING_TASK")) {
        		haveFloatingTasks = true;
                        String floatingTask = (count++ + ". " + taskList.get(i).getDescription());

                        Text floatingDisplay = new Text(WordUtils.wrap(floatingTask, 62, "\n", true));
                        floatingDisplay.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
                        vbox2.getChildren().add(floatingDisplay);
        	}
        }

        if(haveFloatingTasks == false) {
            Text noFloatingTasks = new Text("*none*");
            noFloatingTasks.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
            vbox2.getChildren().add(noFloatingTasks);
        }
        
        sp2.setContent(vbox2);
        v2_2.getChildren().addAll(sp2);
        
        if(haveEventsSpanningDays == true) {
        	VBox v2 = new VBox();
            v2.setSpacing(10);
            v2_1.setPrefHeight(235);
            v2_2.setPrefHeight(235);
        	v2.getChildren().addAll(v2_1, v2_2);
        	main.getChildren().addAll(v1, v2);
        }
        else {
        	main.getChildren().addAll(v1, v2_2);
        }
        
        main.setAlignment(TOP_CENTER);
        
        
        return main;
        
    }
    
    protected HBox addHeader(doordonote.logic.UIState.ListType listType) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(20, 25, 20, 25));
        if(listType.equals(doordonote.logic.UIState.ListType.NORMAL)) {
            hbox.setStyle("-fx-background-color: #0D0D0D;");
        }
        else if(listType.equals(doordonote.logic.UIState.ListType.FINISHED)){
        	hbox.setStyle("-fx-background-color: #000E54;");
        }
        else {
        	hbox.setStyle("-fx-background-color: #560202;");
        }
        
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        
        
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 26));
        title.setFill(Color.WHITE);
        title.setEffect(ds);
        title.setCache(true);
        title.setX(10.0f);
        title.setY(270.0f);	
        
        hbox.getChildren().add(title);
        hbox.setAlignment(CENTER);
        
        return hbox;
    }
    
    protected Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    
    protected String getDay(Calendar cal) {
        String day = null;
        switch(cal.get(cal.DAY_OF_WEEK)) {
                    case 1: day = "Sunday";
                            break;
                    case 2: day = "Monday";
                            break;
                    case 3: day = "Tuesday";
                            break;
                    case 4: day = "Wednesday";
                            break;
                    case 5: day = "Thursday";
                            break;
                    case 6: day = "Friday";
                            break;
                    case 7: day = "Saturday";
                          
                }
        
        return day;
    }
    
    protected String getMonth(Calendar cal) {
        String month = null;
        switch(cal.get(cal.MONTH)) {
                    case 0: month = "Jan";
                            break;
                    case 1: month = "Feb";
                            break;
                    case 2: month = "Mar";
                            break;
                    case 3: month = "Apr";
                            break;
                    case 4: month = "May";
                            break;
                    case 5: month = "Jun";
                            break;
                    case 6: month = "Jul";
                            break;
                    case 7: month = "Aug";
                            break;
                    case 8: month = "Sept";
                            break;
                    case 9: month = "Oct";
                            break;
                    case 10: month = "Nov";
                            break;
                    case 11: month = "Dec";
                }
        
        return month;
    }
    
    protected String getMinutes(Calendar cal) {
        String minutes;
        
        if(cal.get(cal.MINUTE) < 10) {
        	if(cal.get(cal.MINUTE) == 0) {
                minutes = null;
            }
            else {
                minutes = "0" + cal.get(cal.MINUTE);
            }
        }
        else {
            minutes = "" + cal.get(cal.MINUTE);
        }
        
        return minutes;        
    }
    
    protected String getTime(Calendar cal) {
        String time;
        String minutes = getMinutes(cal);
        int hour = cal.get(cal.HOUR_OF_DAY);
        
        if(hour > 12) {
            if(minutes != null) {
                time = (hour - 12) + ":" + minutes + "pm";
            }
            else {
                time = (hour - 12) + "pm";
            }
        }
        else if (hour < 12){
        	if(minutes != null) {
            	if(hour == 0) {
            		
                    time = hour+12 + ":" + minutes + "am";
            	}
            	else{
            		time = hour + ":" + minutes + "am";
            	}
            }
            else {
            	if(hour == 0) {
            		time = hour+12 + "am";
            	}
            	else {
                    time = hour + "am";
            	}
            }
        }
        else {
        	if(minutes != null) {
                time = hour + ":" + minutes + "pm";
            }
            else {
                time = hour + "pm";
            }
        }
        
        return time;  
        
    }
    
    public static String getFirstWord(String input) {
		String commandTypeString = input.trim().split("\\s+")[0];
		return commandTypeString.toLowerCase();
	}
    
    public static boolean checkForToday(Date date) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    	Date today = new Date();
    	boolean isToday = dateFormat.format(date).equals(dateFormat.format(today));
    	return isToday;
    }
    
    public static boolean checkForOverdue(Date date) {
    	Date today = new Date();
    	boolean isOverdue = date.before(today);
    	return isOverdue;
    	
    }
    
    public static boolean checkForOngoing(Date start, Date end) {
    	Date today = new Date();
    	
    	boolean isOngoing = (!today.before(start)) && (!today.after(end));
    	return isOngoing;
    }
    
    public Timeline createBlinker(Node node) {
        Timeline blink = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(
                                node.opacityProperty(), 
                                1, 
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(0.25),
                        new KeyValue(
                                node.opacityProperty(), 
                                0, 
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(0.5),
                        new KeyValue(
                                node.opacityProperty(), 
                                1, 
                                Interpolator.DISCRETE
                        )
                )
        );
        blink.setCycleCount(3);

        return blink;
    }
    
    public FillTransition changeColour(Shape shape, Color color) {
        FillTransition fill = new FillTransition(Duration.seconds(5), shape, Color.web("#DFCA00"), color);
        return fill;
    }
    
    
    /*public static String wrapText(String text) {
    	StringBuilder sb = new StringBuilder(text);

    	int x = 0;
    	while (x + 50 < sb.length() && (x = sb.lastIndexOf(" ", x + 50)) != -1) {
    	    sb.replace(x, x + 1, "\n");
    	}
    	return sb.toString();
    }
    */
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	launch(args);
    }
    
}
