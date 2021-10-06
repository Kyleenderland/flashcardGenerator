import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FlashcardIO extends JFrame implements ActionListener, WindowListener
{
  private static int buttonClickCount;
  private static int currentFlashcard;
  private static int questionNumber;
  private static int fileInput;
  private static int fileLineCounter;
  private static int flashcardNumber;
  private static boolean outOfCards;
  private static boolean didRestart;
  private static String fileInputLine;
  private JPanel mainPanel;
  private JLabel questionLabel;
  private JButton newFlashcardButton;
  private Map<Integer, Flashcard> flashcardMap;
  private java.util.List<Integer> deckSequence;
  private Flashcard copierFlashcard;
  private Iterator flashcardIterate;
  private JOptionPane restartQuizPane;
  private JFileChooser flashcardFileChooser;
  private FileReader fileReader;
  private BufferedReader fileBuffer;
  private File flashcardFile;

  public FlashcardIO()
  {
    super("Java Project 08: Flashcard I/O");
    mainPanel = new JPanel();
    questionLabel = new JLabel( "<html>WELCOME TO FLASHCARD GENERATOR!</html>", SwingConstants.CENTER );
    questionLabel.setFont( new Font( "Serif", Font.PLAIN, 15 ) );
    newFlashcardButton = new JButton( "Start" );

    flashcardFileChooser = new JFileChooser();
    fileInput = flashcardFileChooser.showOpenDialog( null );
    flashcardFile = flashcardFileChooser.getSelectedFile();
    flashcardFileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
    flashcardMap = new HashMap<Integer, Flashcard>();
    copierFlashcard = new Flashcard();
    fileLineCounter = 0;

    outOfCards = false;
    didRestart = false;

    try
    {
      fileReader = new FileReader( flashcardFile.getPath() );
      fileBuffer = new BufferedReader( fileReader );


      while( ( fileInputLine = fileBuffer.readLine() ) != null )
      {
        fileLineCounter++;

        if( (fileLineCounter % 2) == 1 ) //if 'fileLineCounter' is odd-numbered...
        {
          copierFlashcard.setQuestion( fileInputLine );
        } //end 'if'
        if( (fileLineCounter % 2) == 0 ) //if 'fileLineCounter' is even-numbered...
        {
          copierFlashcard.setAnswer( fileInputLine );

          Flashcard copierFlashcardClone = new Flashcard();
          copierFlashcardClone.setQuestion( copierFlashcard.getQuestion() );
          copierFlashcardClone.setAnswer( copierFlashcard.getAnswer() );

          flashcardNumber++;
          flashcardMap.put(flashcardNumber, copierFlashcardClone);
        } //end 'if'

      } //end 'while'
    } //end 'Try'
    catch( FileNotFoundException fnfError )
    {
      fnfError.printStackTrace();
    } //end 1st 'Catch' for 'Try'
    catch ( IOException ioeError )
    {
      ioeError.printStackTrace();
    } //end 2nd 'Catch' for 'Try'

    java.util.List deckSequence = new ArrayList<Integer>( flashcardMap.keySet() );
    Collections.shuffle( deckSequence );
    flashcardIterate = deckSequence.iterator();

    mainPanel.add(questionLabel);
    mainPanel.add(newFlashcardButton);
    mainPanel.setLayout( new GridLayout( 2, 1 ) );

    add( mainPanel );

    setLayout( new GridLayout( 1, 1 ) );
    newFlashcardButton.addActionListener( this );
  }

  public void actionPerformed( ActionEvent ae )
  {
    if( ae.getSource() == newFlashcardButton )
    {
      buttonClickCount++;

      if( outOfCards == true )
      {
        newFlashcardButton.setEnabled( false );
        didRestart = false;

        while( didRestart == false )
        {
          Object[] choices = {"Restart", "Exit" };
          int input = JOptionPane.showOptionDialog(this,
          "Out of flashcards. Would you like to restart or exit?", "OUT OF FLASHCARDS",
          JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
          null, choices, choices[1] );

          switch( input )
          {
            case JOptionPane.YES_OPTION: //procedure for "Restart" option.
            didRestart = true;

            java.util.List deckSequence = new ArrayList<Integer>( flashcardMap.keySet() );
            Collections.shuffle( deckSequence );
            flashcardIterate = deckSequence.iterator();
            newFlashcardButton.setEnabled( true );
            questionNumber = 0;
            outOfCards = false;
            buttonClickCount = 0;

            buttonClickCount++;

            break;
            case JOptionPane.NO_OPTION: //procedure for "Exit" option.
            System.exit( 0 );
            break;
            default:
            break;
          } //end 'switch'
        } //end 'while'
      } //end 'if'

      if( (buttonClickCount % 2) == 0 && outOfCards == false )
      {
        if( flashcardIterate.hasNext() == false ) outOfCards = true; //end 'if'

        newFlashcardButton.setText( "<html><center>" + "Next question" + "</html></center>" );
        questionLabel.setText( "<html>" + "<b>" + flashcardMap.get( currentFlashcard ).getAnswer() + "</b>" + "</html>" );
      } //end 'if'
      else
      {
        questionNumber++;

        newFlashcardButton.setText( "<html><center>" + "Click for Answer" + "</html></center>" );
        currentFlashcard = ( int )flashcardIterate.next();
        questionLabel.setText( "<html>" + "<b>" + "Question " + questionNumber + " of " + flashcardMap.size() + ": " + "</b>"
        + flashcardMap.get( currentFlashcard ).getQuestion() );
      } //end 'else'

    } //end 'if'

  } //end 'actionPerformed'

  public void windowDeactivated( WindowEvent we ){}
  public void windowActivated( WindowEvent we ){}
  public void windowDeiconified( WindowEvent we ){}
  public void windowIconified( WindowEvent we ){}
  public void windowClosed( WindowEvent we ){}
  public void windowOpened( WindowEvent we ){}
  public void windowClosing( WindowEvent we ) {}

  public static void main( String[] args )
  {
    FlashcardIO app = new FlashcardIO();

    app.setSize( 500, 500 );
    app.setVisible( true );
    app.setMinimumSize( new Dimension( 250, 250 ) );
    app.setMaximumSize( new Dimension( 750, 750 ) );
    app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
  } //end 'main'
} //end the public class.
