import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class implements the sender.
 */
public class MessageSender
{
    // maximum transfer unit (frame length limit)
    private final int mtu;
    // Source of the messages.
    private final Scanner stdin;

    /**
     * Create and initialize a new MessageSender.
     *
     * @param mtu the maximum transfer unit (MTU) (the length of a frame must
     * not exceed the MTU)
     */
    public MessageSender(int mtu) {
        this.mtu = mtu;
        this.stdin = new Scanner(System.in);
    }

    /**
     * Read a line from standard input layer and break it into frames
     * that are output on standard output, one frame per line.
     * Report any errors on standard error.
     */
    public void sendMessage() {
        String message = stdin.nextLine();
        if(message != null) {
            // This part needs completing.
            String frame;
            int maxFrameLen = mtu - 8;
            int lenOfMessage = message.length();

            if(lenOfMessage <= maxFrameLen){
                int singleHex = 0;
                for (int i = 0; i<message.length();i++){
                    singleHex = singleHex + message.charAt(i);
                }
                String hex = calculateHex(singleHex);
                frame = "{"+message+";"+hex+";:}";

                System.out.println(frame);
            }else{

                int count = 0;
                ArrayList<String> messageSegments = new ArrayList<>();
                StringBuilder str = new StringBuilder();
                char com = ';';
                for(int i =0;i<lenOfMessage;i++){
                    char cur = message.charAt(i);
                    if(cur==com){
                        if(count+2 <= maxFrameLen){
                            str.append(message.charAt(i));
                            count+=2;
                        }else{
                            messageSegments.add(str.toString());
                            str = new StringBuilder();
                            str.append(message.charAt(i));
                            count=2;
                        }
                    }else {
                        if(count == maxFrameLen){
                            messageSegments.add(str.toString());
                            str = new StringBuilder();
                            str.append(message.charAt(i));
                            count = 1;
                        }else{
                            str.append(message.charAt(i));
                            count++;
                        }
                    }
                }
                messageSegments.add(str.toString());
                ArrayList<String> output = new ArrayList<>();

                String hex2, replaced, frame2;
                for (int i = 0; i<messageSegments.size();i++){
                    int frameHex = 0;
                    if(!(i == messageSegments.size()-1)) {
                        for (int j = 0; j < messageSegments.get(i).length(); j++) {
                            frameHex = frameHex + messageSegments.get(i).charAt(j);
                        }
                        hex2 = calculateHex(frameHex);
                        replaced = messageSegments.get(i).replaceAll(";", ";;");
                        frame2 = "{" + replaced + ";" + hex2 + ";?}";
                        output.add(frame2);
                    }else {
                        for (int j = 0; j < messageSegments.get(i).length(); j++) {
                            frameHex = frameHex + messageSegments.get(i).charAt(j);
                        }
                        hex2 = calculateHex(frameHex);
                        replaced = messageSegments.get(i).replaceAll(";", ";;");
                        frame2 = "{" + replaced + ";" + hex2 + ";:}";
                        output.add(frame2);

                    }
                }

                for (String st : output) {
                    System.out.println(st);
                }
            }
        }
        else {
            System.err.println("No message received.");
        }
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

