import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class implements the receiver.
 * 
 */
public class MessageReceiver {
    // maximum transfer unit (frame length limit)
    private final int mtu;
    // Source of the frames.
    private final Scanner stdin;

    /**
     * Create and initialize new MessageReceiver.
     *
     * @param mtu the maximum transfer unit (MTU)
     */
    public MessageReceiver(int mtu) {
        this.mtu = mtu;
        this.stdin = new Scanner(System.in);
    }


    ArrayList<String> mes = new ArrayList<>();

    /**
     * Receive a single message on stdin, one frame per line
     * and output the recreated message on stdout.
     * Report any errors on stderr.
     */
    public void receiveMessage() {
        boolean endOfMessage = false;

        String message = "";
        do {
            while (stdin.hasNextLine()) {
                String frame = stdin.nextLine();
                if(frame.length() > mtu){
                    endOfMessage = true;
                    error();
                }
                mes.add(frame);
            }
            endOfMessage = true;
        } while (!endOfMessage);
        //decode frames here.

        for (int i = 0; i < mes.size(); i++) {
            String tempFrame = mes.get(i);
            int len = tempFrame.length();

            if (tempFrame.charAt(0) != '{'){
                error();
            }else if (tempFrame.charAt(len-1)!= '}'){
                error();
            } else if (tempFrame.charAt(len-3) != ';'){
                error();
            }else if (tempFrame.charAt(len-7) != ';'){
                error();
            }

            if (i == mes.size()-1){
                if (tempFrame.charAt(len-2) !=':'){
                    error();
                }
            }else {
                if (tempFrame.charAt(len-2) == ':'){
                    error();
                }
            }

            if (i != mes.size()-1){
                if (tempFrame.charAt(len-2) != '?'){
                    error();
                }
            }

            String checksum = tempFrame.substring(len - 6, len - 3);
            String mess = tempFrame.substring(1, len - 7);

            //Checks to make sure ; is ;;
            for (int k = 0; k < mess.length(); k++){
                if (mess.charAt(k) == ';'){
                    if(mess.charAt(k+=1) != ';'){
                        error();
                    }
                }
            }

            mess = mess.replaceAll(";;", ";");
            int frameHex =0;
            for (int j = 0; j< mess.length();j++){
                frameHex = frameHex + mess.charAt(j);
            }
            if(!calculateHex(frameHex).equals(checksum)){
                error();
            }

            message = message + mess;


        }
        System.out.println(message);
    }

    public void error(){
        System.err.println("Error with frame formatting.");
        System.exit(0);
    }

    public String calculateHex(int frameHex){
        String hexVal = Integer.toHexString(frameHex);
        String formattedHex = hexVal;
        int lenOfHex = hexVal.length();
        if (lenOfHex >3){
            formattedHex = hexVal.substring(hexVal.length()-3);
        }else if (lenOfHex == 2){
            formattedHex = "0"+hexVal;
        }else if(lenOfHex == 1){
            formattedHex = "00"+hexVal;
        }
        return formattedHex;
    }


}

