package astar;


import java.util.*;

public class ArrayHeap<T> implements ExtrinsicMinPQX<T> {
    ArrayList<PriorityNode> data ;
    HashMap<T,Double> datamap = new HashMap<>();

    public ArrayHeap() {
        data = new ArrayList<PriorityNode>();
    }



    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode temp =  data.get(a).copy();
        data.get(a).item = data.get(b).item;
        data.get(a).priority = data.get(b).priority;
//        data.get(a).posIndex = data.get(b).posIndex;
        data.get(b).item = temp.item;
        data.get(b).priority = temp.priority;
//        data.get(b).posIndex = temp.posIndex;
    }
    private void swap(PriorityNode a, PriorityNode b) {
//        PriorityNode temp = (a).copy();
        T titem = a.item;
        double tpriority = a.priority;
        (a).item = (b).item;
        (a).priority = (b).priority;
//        a.posIndex = b.posIndex;
        (b).item = titem;
        (b).priority = tpriority;
//        b.posIndex = temp.posIndex;
    }
    private int getleft(int ofIndex){ return (2*ofIndex)+1;}
    private int getright(int ofIndex){ return (2*ofIndex)+2;}
    private int getparent(int ofIndex){ return (ofIndex-1)/2;}

    private void trickleUp(int index){
        if (data.size() == 0 || index == 0) return;
        int insertPoint = index;
        int nextInsertPoint = insertPoint;
        while(insertPoint >0 && data.get(insertPoint).compareTo(data.get(getparent(insertPoint))) <= 0){
            nextInsertPoint = getparent(insertPoint);
            swap(insertPoint,nextInsertPoint);
            insertPoint = nextInsertPoint;
        }
    }
    private void heapify(int index){
        int currentIndex = index;
        while( currentIndex >=0 && getleft(currentIndex)  < data.size() ){
           currentIndex = Heapify(currentIndex);
        }
    }
    private int Heapify(int index){
        if (data.size() == 0) return -1;
        if( getleft(index)  >= data.size() ) return -1 ;

        PriorityNode root = data.get(index);
        PriorityNode leftChild = null;
        int leftInd;
        PriorityNode rightChild = null;
        int rightInd;

        leftInd = getleft(index);
        if ( leftInd < data.size() ) {
            leftChild = data.get(leftInd);
        }
        rightInd = getright(index);
        if( rightInd < data.size() ) {
            rightChild = data.get(rightInd);
        }

//
       /* leftInd = getleft(index);
        if ( leftInd < data.size() ) {
            leftChild = data.get(leftInd);
            if (leftChild.item.equals(item)) return leftChild;
            if (Double.compare(leftChild.priority,priority) <= 0 ) return finditem(item,priority,leftInd);
        }
        rightInd = getright(index);
        if( rightInd < data.size() ){
            rightChild = data.get(rightInd);
            if(rightChild.item.equals(item)) return rightChild;
            if (Double.compare(rightChild.priority,priority) <= 0 ) return finditem(item,priority,rightInd);
        }*/
//
        if ((leftChild == null && rightChild == null) ||
                root.compareTo(leftChild) <= 0 && root.compareTo(rightChild  ) <= 0) return -1;

        else if (leftChild.compareTo(rightChild) <= 0) {
            swap(root, leftChild);
            return leftInd;
//            heapify(getleft(index));
        } else {
            swap(root, rightChild);
           return rightInd;
//            heapify(getright(index));
        }

    }
    /**
     * Adds an item with the given priority value.
     * Assumes that item is never null.
     * Runs in O(log N) time (except when resizing).
     * @throws IllegalArgumentException if item is already present in the PQ
     */
    @Override
    public void add(T item, double priority) {
        if( datamap.get(item) == null ) {
            PriorityNode newNode = new PriorityNode(item,priority,data.size());

            datamap.put(item,priority);
            data.add(newNode);
            int insertPoint = data.size()-1;
            int nextInsertPoint = insertPoint;
            while(insertPoint >0 && data.get(insertPoint).compareTo(data.get(getparent(insertPoint))) <= 0){
                nextInsertPoint = getparent(insertPoint);
                swap(insertPoint,nextInsertPoint);
                insertPoint = nextInsertPoint;
            }

//          heapify(insertPoint);
        }
        else{ throw new IllegalArgumentException(item.toString()+" Already Exists in Heap.");}
    }
    public PriorityNode findItem(T item,double priority){
        if (! contains(item) ) return null;
        if( data.get(0).item.equals(item)) return data.get(0);
        return finditem(item,priority,0);
    }
    public PriorityNode finditem(T item,double priority,int index){

//        if (! contains(item) ) return null;
//        if( data.get(index).item.equals(item)) return data.get(index);
        if( getleft(index)  >= data.size() ) return null;

        int leftInd;
        int rightInd;
        PriorityNode leftChild = null;
        PriorityNode rightChild = null;


            leftInd = getleft(index);
            if ( leftInd < data.size() ) {
                leftChild = data.get(leftInd);
                if (leftChild.item.equals(item)) return leftChild;
                if (Double.compare(leftChild.priority,priority) <= 0 ) return finditem(item,priority,leftInd);
            }
            rightInd = getright(index);
            if( rightInd < data.size() ){
                rightChild = data.get(rightInd);
                if(rightChild.item.equals(item)) return rightChild;
                if (Double.compare(rightChild.priority,priority) <= 0 ) return finditem(item,priority,rightInd);
            }


//        if (index < data.size() ){
//            if(leftChild != null && Double.compare(leftChild.priority,datamap.get(item)) <= 0){
//                leftChild = finditem(item,getleft(index));
//                if(leftChild != null  && leftChild.item.equals(item)) return leftChild;
//            }
//            if(rightChild != null && Double.compare(rightChild.priority,datamap.get(item) ) <= 0){
//                rightChild = finditem(item,getright(index));
//                if(rightChild != null  && rightChild.item.equals(item)) return rightChild;
//            }
//
//        }


        return null;
    }

    /**
     * Returns true if the PQ contains the given item; false otherwise.
     * Runs in O(log N) time.
     */
    @Override
    public boolean contains(T item) {

        if (datamap.get(item) != null) return true;
        return false;
    }

    /**
     * Returns the item with the smallest priority.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T getSmallest() {
        return data.get(0).item;
    }

    /**
     * Removes and returns the item with the smallest priority.
     * Runs in O(log N) time (except when resizing).
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T removeSmallest() {
        if(datamap.size() > 0) {
            datamap.remove(data.get(0).item);
            swap(data.get(0), data.get(data.size() - 1));
            PriorityNode temp = data.remove(data.size() - 1);
            heapify(0);
            return temp.item;
        }
        else return null;
    }

    /**
     * Changes the priority of the given item.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    @Override
    public void changePriority(T item, double priorityWas,double priorityTo) {
        PriorityNode changeNode = findItem(item,priorityWas);

        if (changeNode == null) throw new NoSuchElementException(item+"Is not found");

        changeNode.priority = priorityTo;
        datamap.put(item,priorityTo);
        if (changeNode.compareTo(data.get(getparent(changeNode.posIndex))) <0 ){
            trickleUp(changeNode.posIndex);
        }
        else if((getleft(changeNode.posIndex) < data.size() && changeNode.compareTo(data.get(getleft(changeNode.posIndex))) > 0 ) ||
                (getright(changeNode.posIndex) < data.size() &&  changeNode.compareTo(data.get(getright(changeNode.posIndex))) > 0 )){
            heapify(changeNode.posIndex);
        }
    }

    /**
     * Returns the number of items in the PQ.
     * Runs in O(log N) time.
     */
    @Override
    public int size() {
        return data.size();
    }


    class PriorityNode implements Comparable<ArrayHeap.PriorityNode> {
        private T item;
        private double priority;
        private int posIndex = -1;
        PriorityNode(T e, double p,int posIndex) {
            this.item = e;
            this.priority = p;
            this.posIndex = posIndex;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        int getPos() {
            return posIndex;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(ArrayHeap.PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((ArrayHeap.PriorityNode) o).getItem().equals(getItem());
            }
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
        public PriorityNode copy(){
            return new PriorityNode(this.item,this.priority,this.posIndex);
        }
    }

    public Object[] getArray(){
        Object[] temp = new Object[data.size()];
        int i = 0;

        for(PriorityNode each : data){
            temp[i++] = each.item;
        }
        return temp;
    }
    public Object[] getOrderedArray(){
        ArrayHeap<T> temp = new ArrayHeap<>();
        temp.datamap = (HashMap<T, Double>) this.datamap.clone();

        Object[] tempArr = new Object[this.size()];

        for(PriorityNode each : data){
            temp.data.add(each.copy());
        }
        int i = 0;
        while(temp.size()>0){
            tempArr[i] = temp.removeSmallest();
            i++;
        }
        return  tempArr;
    }

}
