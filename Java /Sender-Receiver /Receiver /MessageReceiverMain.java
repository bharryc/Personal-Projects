/**
 * Main class for running the MessageReceiver.
 * The MTU value is received via the command-line arguments.
 * 
 * @author cssb2
 */
public class MessageReceiverMain
{
    public static void main(String[] args)
    {
        int mtu = Integer.MIN_VALUE;
        int argnum = 0;
        boolean argsOk = true;
        while(argnum < args.length && argsOk) {
            switch(args[argnum]) {
                case "--mtu":
                    argnum++;
                    if(argnum < args.length) {
                        mtu = Integer.parseInt(args[argnum]);
                        argnum++;
                    }
                    else {
                        System.err.println("Illegal MTU: " + args[argnum]);
                        argsOk = false;
                    }
                    break;
                default:
                    System.err.println("Unknown argument: " + args[argnum]);
                    argsOk = false;
            }
        }
        if(!argsOk) {
            
        }
        else if(mtu == Integer.MIN_VALUE) {
            System.err.println("No MTU specified.");
        }
        else {
            try {
                MessageReceiver receiver = new MessageReceiver(mtu);
                receiver.receiveMessage();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        System.out.flush();
        System.err.flush();
    }

}
