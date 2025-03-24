import java.util.Iterator;
/**
 * A class to model a simple email client. The client is run by a
 * particular user, and sends and retrieves mail via a particular server.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class MailClient
{
    // The server used for sending and receiving.
    private MailServer server;
    // The user running this client.
    private String user;

    private MailItem lastRecievedMail;
    
    private int numMensajesRecibidos = 0;
    private int numMensajesEnviados = 0;
    private String mensajeLargoRemitente = " ";
    private int numCaracteresMensajesLarga = 0;
  

    /**
     * Create a mail client run by user and attached to the given server.
     */
    public MailClient(MailServer server, String user)
    {
        this.server = server;
        this.user = user;
    }

    /**
     * Return the next mail item (if any) for this user.
     */
    public MailItem getNextMailItem()
    {   
        MailItem item = server.getNextMailItem(user);
        
        
        if(item != null) {
            String mensaje = item.getMessage().toLowerCase();
            String asunto = item.getSubject().toLowerCase();
            String persona = user.toLowerCase();
            boolean asuntoConNombrePersona = asunto.contains(persona);
            
            if((mensaje.contains("loteria" ) || mensaje.contains("viagra" )) && asuntoConNombrePersona == false) {
                return null;
            }
            
            int longitudMensaje = 0;
            
            if(numCaracteresMensajesLarga < mensaje.length()) {
                numCaracteresMensajesLarga = mensaje.length();
                
                mensajeLargoRemitente = item.getFrom();
            }
            lastRecievedMail = item;
            numMensajesRecibidos++;
             
        }
        
        return item;
    }

    /**
     * Print the next mail item (if any) for this user to the text 
     * terminal.
     */
    public void printNextMailItem()
    {
        MailItem item = getNextMailItem();
        
        if(item == null) {
            System.out.println("No new mail.");
        }
        else {
            item.print();
        }
    }

    /**
     * Send the given message to the given recipient via
     * the attached mail server.
     * @param to The intended recipient.
     * @param message The text of the message to be sent.
     */
    public void sendMailItem(String to, String subject, String message)
    {
        MailItem item = new MailItem(user, to, subject, message);
        server.post(item);
        numMensajesEnviados++;
    }
    
   
    public int getNumberOfMessageInServer(){
        numMensajesRecibidos = server.howManyMailItems(user);
        return numMensajesRecibidos;
    }
    
    public void receiveAndAutorespond(){
        MailItem item = getNextMailItem();
       
        if(item != null) {
            lastRecievedMail = item;
            String quienRecibe = lastRecievedMail.getTo();
            String remitente = lastRecievedMail.getFrom();
            String asuntoMensaje = "RE: " + lastRecievedMail.getSubject ();
            String contenidoRespuesta = "Gracias por su mensaje. Le contestare lo antes posible. " + lastRecievedMail.getMessage();
            
            sendMailItem(remitente, asuntoMensaje, contenidoRespuesta);
            numMensajesEnviados++;
        }
    }
    
    public String getStatus() {
        if(numMensajesRecibidos == 0) {
            return "0,0,,";
        }else {
            return numMensajesRecibidos + "," + numMensajesEnviados + "," + mensajeLargoRemitente + "," + numCaracteresMensajesLarga;
        }
    }
    
    public MailItem getLastReceivedMail() {
        return lastRecievedMail;
    }
}
        

