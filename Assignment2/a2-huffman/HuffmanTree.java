import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

/**
 * HuffmanTree creates and stores a Huffman tree based on Huffman nodes (an inner class),
 * It also provide a series of methods for encoding and decoding.
 * It uses a BitFeedOut which allows a stream of bits be sent continuously
 * to be used for encoding.
 * It also uses an Iterator<Byte> which allows a stream of bits to be read continuously
 * to be used when decoding.
 *
 * @author Lucia Moura
 */

public class HuffmanTree {
    
    public static int EndOfText=((int)'\uffff')+1; //special symbol created to indicate end of text
    
    HuffmanNode root=null; // root of the Huffman tree
    HuffmanNode[] leafWhereLetterIs;   // array indexed by characters, storing a reference to
    // the Huffman Node (leaf) in which the character is stored
    
    
    // Constructor receives frequency information which is used to call BuildTree
    public HuffmanTree (LetterFrequencies letterFreq) {
        
        root=BuildTree (letterFreq.getFrequencies(),letterFreq.getLetters());
        
    }
    
    // BuildTree builds the Huffman tree based on the letter frequencies
    
    private HuffmanNode BuildTree(int[] frequencies,char[] letters) {
        
        
        /******** STEP 2 of Algorithm Huffman(X) **********************************/
        // we use a priority queue to store frequencies of subtrees created
        // during the construction of the Huffman tree
        HeapPriorityQueue<HuffmanNode, HuffmanNode> heap =
        new HeapPriorityQueue<HuffmanNode, HuffmanNode>(frequencies.length+1);
        int count= 0;
        // initialize array leftWhereLetterIs
        leafWhereLetterIs =new HuffmanNode[(int)'\uffff'+2]; // need 2^16+1 spaces
        for (int i=0; i< (int)'\uffff'+2; i++)
            leafWhereLetterIs[i]=null;
        
        /********* STEPS 3-5 of Algorithm Huffman(X) **********************************/
        // creating one node per letter as a single tree inserted into the priority queue
        for (int i=0; i<frequencies.length; i++) {
            if (frequencies[i]>0) {
                count++;
                HuffmanNode node= new HuffmanNode( (int)letters[i], frequencies[i],null,null,null);
                leafWhereLetterIs[(int)letters[i]]=node;
                heap.insert(node,node);
                
            }
        }
        // creating node for "EndOfText" special symbol
        HuffmanNode specialNode= new HuffmanNode( EndOfText,0,null,null,null);
        leafWhereLetterIs[EndOfText]=specialNode; // last position reserved
        heap.insert(specialNode,specialNode);
        
        
        /************ STEPS 6-13 of Algorithm Huffman(X): task to be implemented ************/
        while(heap.size() > 1){
            Entry <HuffmanNode,HuffmanNode>e1=heap.removeMin();
            Entry <HuffmanNode,HuffmanNode>e2=heap.removeMin();
            HuffmanNode newT=new HuffmanNode(0,0,null,e1.getKey(),e2.getKey());
            e1.getKey().setParent(newT);
            e2.getKey().setParent(newT);
            int temp = e1.getKey().getFrequency()+e2.getKey().getFrequency();
            newT.setFrequency(temp);
            heap.insert(newT,newT);
        }
        Entry<HuffmanNode,HuffmanNode>e=heap.removeMin();
        /******       HERE YOU MUST IMPLEMENT THE REST OF ALGORITHM HUFFMAN(X)  ************/
        
        return e.value; /***** this must be altered to return the root of the tree build by steps 6-13 ****/
        
    }
    
    // encodeCharacter encodes the character c using the Huffman tree
    // returning its encoding as String of 0s and 1s representing the bits
    // In the handout example if c='G' this method will return "011"
    
    private String encodeCharacter(int c)  {
        
        /*** Step in this method to be implemented by students
         *
         *
         * Encode the character into its codeword using the Huffman tree.
         * Remember that the algorithm must run in O(L)
         * where L is the size of the codeword generated
         *
         *
         ****/
        /*** note this is returning a wrong output (always 0)***/
        /*
         Method compressing given unicode character c into huffman frequences.
         The method consist a run time of O(n). n stands for the given sizes of the tree
         The application is taking given character and search in the pre-organized array
         that store the HuffmanNode of all the leafs. Applied the uphead searching implementation
         to find the proper routine to reach the root.
         Example:
         >>> encodeCharacter(65 [unicode stands for 'A'])
         >>> 1
         >>> encodeCharacter(71 [unicode stands for 'G'])
         >>> 011
         
         return Type String -> the encoded sequencial bits of the given integer c in current Tree.
         */
        StringBuilder result= new StringBuilder();
        HuffmanNode post = leafWhereLetterIs[c];
        Stack resultinOppOrder= new Stack();
        while(post != root){
            if ( post ==post.parent().leftChild()){
                resultinOppOrder.push("0");
                post=post.parent();
            }else{
                resultinOppOrder.push("1");
                post=post.parent();
            }
        }
        while(!resultinOppOrder.isEmpty()){
            result.append(resultinOppOrder.pop());
        }
        
        return result.toString();
    }
    
    // Encode the a character c using the Huffman tree
    // sending the encoded bits to argument BitFeedOut bfo
    // (please do not change this method)
    
    public void encodeCharacter (int c, BitFeedOut bfo) {
        String s=encodeCharacter(c);
        for (int i=0; i< s.length();i++) bfo.putNext(s.charAt(i));
        
    }
    
    // decodeCharacter receives Iterator<Byte> bit that iterates through a sequence
    //  of bits of the  encoded string; this sequence must be
    // compatible with the Huffman tree (has been previously generated by
    // a tree like this one.
    // This method will be "consuming" bits until it completes the
    // decoding of a letter which is then returned.
    // In the handout example, if the next bits are 011001...
    // decodeCharacter will apply bit.next() 3 times until if decodes
    // the first letter, which in this case is 'G'
    /**** Steps of this method to be implemented by students ****/
    
    /***
     *
     * Decodes sequence of next bits returned by several
     * calls to bit.next() until it completes the decoding of the next character
     *
     *  Note1: the return value of bit.next() is a Byte but should be interpreted as a bit;
     *  it must be either 0 or 1.
     *  Note2: the return value is an integer (unicode) of the character; for a character
     *  char c this can be obtained by casting the character: (int) c
     *
     * Remember that the algorithm must run in O(L)
     * where L is the size of the codeword for the character
     *
     *
     */
    public int decodeCharacter(Iterator<Byte> bit) {
        /*
         *pre-condition: the bit must be construct with method BuildTree(int,BitFeedOut)
         *				  and encoded with encodeCharacter(int,BitFeedOut).
         *
         *	bit != null else throw NullPointerException.
         *
         decodeCharacter(Iterator<Byte> bit)
         Arguement is a flow of arraylist that store bits of 0 and 1.
         bit is pre-compressed Huffman information. We used the given sequence to
         run over the tree to find the proper characters that was stored in the leaves.
         
         logically, we used the condition 0 and 1 to determine the direction of the iterator.
         Whenever it reaches the leaf, the travel would start-over.
         
         The run-time-complexity is O(logn). This is because the travel route would start at root
         and run half of the tree. Thus is O(log base 2 n). n stands for the size of the tree.
         
         >>>decodeCharacter(110101011001110011000111101010100)
         >>>AACGTAAATAATGAAC
         
         >>>decodeCharacter(10000010000100110110011110111111101011111110101000001101110101000100101010100110110011010111011100101100100)
         >>>Hey,this is my second test!
         
         */
        if (root == null) return Integer.MAX_VALUE; // empty tree is not valid when decoding
        HuffmanNode iterator = root;
        while( !iterator.isLeaf()){
            if(bit.next() == 0){
                iterator=iterator.left;
            }else{
                iterator=iterator.right;
            }
        }
        int c=iterator.getLetter();
        
        return c; /*** this needs to be changed since it is returning a
                   wrong output (always code of special character EndOfText)***/
    }
    
    
    
    // auxiliary methods for printing the codes in the Huffman tree
    
    void printCodeTable() {
        System.out.println("**** Huffman Tree: Character Codes ****");
        if (root!=null)
            traverseInOrder(root,""); // uses inorder traversal to print the codes
        else
            System.out.println("No character codes: the tree is still empty");
        System.out.println("***************************************");
        
    }
    
    // In-order traversal of the Huffman tree keeping track of
    // the paths to leaves so it can print the codeword for each letter
    private void traverseInOrder(HuffmanNode current, String c) {
        if (current.isLeaf()) {
            if (current.getLetter()!=EndOfText)
                System.out.println((char)current.getLetter()+":"+c);
            else   System.out.println("EndOfText:"+c);
        }
        else {
            traverseInOrder(current.leftChild(),c+"0");
            traverseInOrder(current.rightChild(),c+"1");
        }
        
    }
    
    // provided byte encoding of the frequency information
    // in the format of 4 bytes per letter
    // 2 first bytes represent letter 2 last bytes represent frequency
    // This is useful for file decoding where the letter frequencies need
    // to be stored in a "header" of the encoded file
    // (not used in the current version of the assignment)
    
    byte[] freqsToBytes() {
        int b=0;
        byte [] treeBytes= new byte[(int)'\uffff'*4];
        for (int i=0;i<'\uffff';i++) {
            if (leafWhereLetterIs[i]!=null) {
                int freq=leafWhereLetterIs[i].getFrequency();
                char letter=(char)leafWhereLetterIs[i].getLetter();
                treeBytes[b++]= (byte)(((int)letter)/256);
                treeBytes[b++]= (byte)(((int)letter)%256);
                treeBytes[b++]= (byte)(freq/256);
                treeBytes[b++]= (byte)(freq%256);
            }
        }
        return Arrays.copyOf(treeBytes, b);
    }
    
    /**** inner class to Huffman tree that implements a Node in the tree ****/
    // nothing to be changed in this inner class
    public class HuffmanNode implements Comparable<HuffmanNode> {
        
        int letter; // if the node is a leaf it will store a letter, otherwise it store null
        int frequency; // stores the sum of the frequencies of all leaves of the tree rooted at this node
        private HuffmanNode parent, left, right; // reference to parent, left and right nodes.
        
        public HuffmanNode() {
            parent=left=right=null;
            frequency=-1;
        }
        
        public HuffmanNode(int letter, int frequency, HuffmanNode parent, HuffmanNode left, HuffmanNode right) {
            this.letter= letter;
            this.frequency=frequency;
            this.parent=parent; 
            this.left=left;
            this.right=right;
        }
        
        
        boolean isLeaf() { return (left==null && right==null);}
        
        // getter methods
        
        HuffmanNode leftChild() { return left;}
        
        HuffmanNode rightChild() { return right;}
        
        HuffmanNode parent() { return parent;}
        
        int getLetter() {return letter;}
        
        int getFrequency() {return frequency;}
        
        // setter methods
        
        void setLeftChild(HuffmanNode leftVal) { left=leftVal;	}
        
        void setRightChild(HuffmanNode rightVal) { right=rightVal;	}
        
        void setParent(HuffmanNode parentVal) { parent=parentVal;	}
        
        void setLetter(char letterVal) { letter = letterVal;}
        
        void setFrequency(int freqVal) { frequency = freqVal; }
        
        @Override
        public int compareTo(HuffmanNode o) {
            if (this.frequency==o.frequency) {
                return this.letter-o.letter;
            }
            else return this.frequency-o.frequency;
            
        }
        
    }
    
    
    
}

