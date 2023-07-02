# JAVA sender reciever project.

This was a university assignemt for a Computer Networks and Communicatons module. The assignemt was to implement a sender and reciever to send and recieve messages in frames using hte univesity network. The message being sent had constraints to stick to, MTU (maximum transfer unit) was the length that the frame could not exceed this included the message and the frame formatting.

The frame itself had strict formatting rules that it had to follow. The frame had to start and end with {}, the frame had to include the message, a 3 digit checksum and an indicator to see if the frame was the last in the transmission or not. This was shown with a ? for a frame with more to follow or : for the final frame. Each segment of the frame had to be seperated with a ;. If the message contained a ; then it had to be doubled in order to avoid the frame formatting ;. For example a message of 'a ; semicolon;' would now be 'a ;; semicolon;;' and would then be broken up into the correctly formatted frames. 

The reciever reveresed this process. It took correctly formatted frames and a MTU and compared checksums with its own caluclations, and reassembled the string on the other side. It took account for the double ; as well as the end of transmission indentifier. 

