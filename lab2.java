
    public class lab2 {

        private Node root;
        private final int MAXSIZE = 4;
        public lab2(){
            root = null;
        }
        public boolean insert( int key){

            Node newNode = new Node();
            newNode.value = key;
            if(root == null){
                root = newNode;
                root.subTree_size+= 1;
                root.nodeKeys[0] = root;
                return true;
            }

            return root.isFound(newNode);
        }

        public int size( int key) {
            Node temp = root.findKey(key);
            return temp  == null ? 0 : temp.subTree_size;
        }

        private class Node {

            private Node[] childrenNode;
            private Node[] nodeKeys;
            private int size;
            private Node parent;
            private int value;
            private int subTree_size;

            public Node() {
                size = 1;
                nodeKeys = new Node[MAXSIZE - 1];
                childrenNode = new Node[MAXSIZE];
                value = 0;
                parent = null;
                subTree_size = 0;
            }

            private boolean isFound(Node newNode) {
                int i = 0;
                this.subTree_size++;
                while ( i < this.size && this.nodeKeys[i].value <= newNode.value) {

                    if (this.nodeKeys[i].value == newNode.value) {
                        this.subTree_size--;
                        return false;
                    }
                    i++; }
                if (this.isLeaf())
                    this.addValue(newNode.value);
                else
                    this.childrenNode[i].isFound(newNode);

                return true;
            }

            private boolean isFull(Node currentNode) {

                return currentNode.size == MAXSIZE - 1 ? true : false;
            }

            private void addValue( int key) {
                this.size++;
                Node newNode = new Node();
                newNode.value = key;
                this.nodeKeys[this.size - 1] = newNode;
                this.nodeKeys[this.size - 1].subTree_size = this.subTree_size;

                this.sortNodeKeys();
                if (this.isFull(this)) {
                    this.split();
                }
            }
            private void sortNodeKeys() {
                for (int i = 1; i < this.size; i++) {
                    int j = i - 1;
                    int next = this.nodeKeys[i].value;
                    while (j >= 0 && this.nodeKeys[j].value > next) {

                        this.nodeKeys[j + 1].value = this.nodeKeys[j].value;
                        j--;
                    }
                    this.nodeKeys[j + 1].value = next;
                }
                this.value = this.nodeKeys[0].value;

            }
            private boolean isLeaf() {
                return this.childrenNode[0] == null;
            }

            private void split() {
                if (this == root) {
                    this.splitRoot();
                } else if (this.isLeaf()) {//means it's at the leaves
                    int i = 0;
                    int key = this.nodeKeys[this.size - 2].value;
                    i = getSizeOfChildrenList(this, i); // find where to insert the new value
                    this.cleanCurrentSize();
                    this.insertChildrenList(i); // insert the split value to the current children list
                    // this.subTree_size -= 1;
                    this.parent.addValue(key);//mid value gose up

                } else if (!this.isLeaf()) {
                    int key = this.nodeKeys[this.size - 2].value;
                    int i = 0;
                    i = getSizeOfChildrenList(this, i); // find where to insert the new value
                    insertChildrenList( i);
                    i = 0;
                    while (this.parent.childrenNode[i].value != this.value) {
                        i++;
                    }
                    this.parent.childrenNode[i].subTree_size = 1;
                    for (int j = 0; j < 2; j++) {
                        this.parent.childrenNode[i].childrenNode[j] = this.childrenNode[j];
                        this.parent.childrenNode[i].subTree_size += this.childrenNode[j].subTree_size;
                        this.makeNewParent(this, j);
                    }
                    this.parent.childrenNode[i + 1].subTree_size = 1;
                    for (int j = 2; j < 4; j++) {
                        this.parent.childrenNode[i + 1].childrenNode[j - 2] = this.childrenNode[j];
                        this.parent.childrenNode[i + 1].subTree_size += this.childrenNode[j].subTree_size;
                        this.parent.childrenNode[i + 1]. makeNewParent(this.parent.childrenNode[i + 1], j - 2);
                    }
                    this.cleanChildrenList(this);
                    // cleanCurrentList(currentNode, 0, 1 );
                    this.parent.addValue(key);//mid value gose up
                }
            }
            public int getSizeOfChildrenList(Node currentNode, int i) {
                while (currentNode.parent.childrenNode[i] != null) {
                    i++;
                }
                return i;
            }
            private void sortChildren(Node current, int size) {
                int temp = current.childrenNode[size - 1].value;
                for (int i = 1; i < size; i++) {
                    int j = i - 1;
                    Node next = current.childrenNode[i];
                    while (j >= 0 && current.childrenNode[j].value > next.value) {
                        current.childrenNode[j + 1] = current.childrenNode[j];
                        j--;
                    }
                    current.childrenNode[j + 1] = next;
                }
                int j = 0;
                while (j < 4) {
                    if (temp == current.childrenNode[j].value) {
                        break;
                    }
                    j++;
                }
                current.childrenNode[j].goToList( 0);
            }

            private void cleanCurrentList() {

                for (int i = MAXSIZE - 2; i >= 1; i--) {
                    this.nodeKeys[i] = null;
                    this.size--;
                }
            }
            private void decrement_subSize(){
                if(this.parent == null){
                    this.subTree_size --;
                    return;
                }
                this.subTree_size --;;
                this.parent.decrement_subSize();;
            }
            private void insertChildrenList( int i) {
                // find where to insert the new value
                this.parent.childrenNode[i] = this.nodeKeys[this.size - 1];
                this.parent.childrenNode[i].goToList( 0);
                // while(  true ) { //if parent is not null, then we need to find it
                if (this.parent.childrenNode[i].parent == null) {
                    this.parent.makeNewParent(this.parent, i); // make a new parent
                }
                // }
                sortChildren(this.parent, i + 1);
                this.cleanCurrentList();
            }

            private void splitRoot() {

                if (this.isLeaf()) {
                    this.swap();
                    for(int i = 0; i< 2; i++){
                        Node newNode = new Node();
                        newNode.value =this.nodeKeys[i + 1].value;
                        this.childrenNode[i] = newNode;
                        this.childrenNode[i].subTree_size += 1;
                        this.childrenNode[i].goToList(0);
                        this.makeNewParent(this, i);
                    }
                    this. cleanCurrentList();
                } else {
                    this.swap();
                    Node [] tempList = new Node[2];
                    for( int i = 0; i < 2; i++){
                        tempList[i]  = this.nodeKeys[i+1];
                        tempList[i].goToList(0);
                        this.makeNewParent(tempList[i], i);
                    }
                    tempList[0].subTree_size = 1;
                    for( int i = 0; i < 2; i++){
                        tempList[0].childrenNode[i] = this.childrenNode[i];
                        tempList[0].subTree_size += this.childrenNode[i].subTree_size;
                        tempList[0].childrenNode[i].goToList(0);
                        tempList[0].makeNewParent(this,i);
                        this.childrenNode[i] = null;
                    }
                    this.cleanCurrentList();
                    tempList[1].subTree_size = 1;
                    for( int i = 2; i < 4; i++){
                        tempList[1].childrenNode[i - 2] = this.childrenNode[i];
                        tempList[1].subTree_size += this.childrenNode[i].subTree_size;
                        tempList[1].childrenNode[i - 2].goToList(0);
                        tempList[1].makeNewParent(this,i-2);
                        this.childrenNode[i] = null;
                    }
                    for( int i = 0; i < 2; i ++){
                        this.childrenNode[i] = tempList[i];
                        this.makeNewParent(this,i);
                    }

                }
            }
            private void cleanCurrentSize(){
                this.subTree_size = 1;
                for( int i = 0; i < this.size; i++){
                    this.nodeKeys[i].subTree_size = 1;
                }
            }
            // swap the mid value of the list with min value
            private void swap(){
                int temp = this.value;
                this.value = this.nodeKeys[1].value;
                this.nodeKeys[1].value = temp;
            }
            private void goToList( int size) {
                Node newNode = new Node();
                newNode.value = this.value;
                this.nodeKeys[size] = newNode;
            }
            private void makeNewParent(Node current, int size) {
                this.childrenNode[size].parent = this;
            }
            private void cleanChildrenList(Node parentNode) {
                for (int i = 3; i > 1; i--) {
                    parentNode.childrenNode[i] = null;
                }
            }
            private Node findKey(int key) {
                int i = 0;
                while(i < this.size && this.nodeKeys[i].value < key){
                    i++;
                }
                if( i < this.size && this.nodeKeys[i].value == key){
                    return this;
                }
                if(this.isLeaf()) {
                    return null;
                }
                return this.childrenNode[i].findKey(key);
            }
        }



}
