package heap;

import java.util.*;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    ArrayList<PriorityNode> data ;
    HashMap<T,Double> datamap = new HashMap<>();

    public ArrayHeapMinPQ() {
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
        PriorityNode temp = (a).copy();
        (a).item = (b).item;
        (a).priority = (b).priority;
//        a.posIndex = b.posIndex;
        (b).item = temp.item;
        (b).priority = temp.priority;
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
            if (data.size() == 0) return;
            PriorityNode root = data.get(index);
            PriorityNode leftChild = null;
            PriorityNode rightChild = null;
            try {
                leftChild = data.get(getleft(index));
            } catch ( IndexOutOfBoundsException e){  }
            try {
                rightChild = data.get(getright(index));
            } catch ( IndexOutOfBoundsException e){  }


                if ((leftChild == null && rightChild == null) ||
                        root.compareTo(leftChild) <= 0 && root.compareTo(rightChild  ) <= 0) return;

                else if (leftChild.compareTo(rightChild) <= 0) {
                    swap(root, leftChild);
                    heapify(getleft(index));
                } else {
                    swap(root, rightChild);
                    heapify(getright(index));
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

    public PriorityNode findItem(T item,int index){
        if (! contains(item) ) return null;
        if( data.get(index).item == item) return data.get(index);

        PriorityNode root = data.get(index);
        PriorityNode leftChild = null;
        PriorityNode rightChild = null;
        T leftItem ;
        T rightItem;
        try {
            leftChild = data.get(getleft(index));
        } catch ( IndexOutOfBoundsException e){  }
        try {
            rightChild = data.get(getright(index));
        } catch ( IndexOutOfBoundsException e){  }

       if (index < data.size() ){
            if(leftChild != null && leftChild.priority <= datamap.get(item)){
                leftChild = findItem(item,getleft(index));
                if(leftChild != null  && leftChild.item == item) return leftChild;
            }
            if(rightChild != null && rightChild.priority <= datamap.get(item)){
                rightChild = findItem(item,getright(index));
                if(rightChild != null  && rightChild.item == item) return rightChild;
            }

        }


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
    public void changePriority(T item, double priority) {
        PriorityNode changeNode = findItem(item, 0);

        if (changeNode == null) throw new NoSuchElementException(item+"Is not found");

        changeNode.priority = priority;
        datamap.put(item,priority);
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


    class PriorityNode implements Comparable<ArrayHeapMinPQ.PriorityNode> {
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
        public int compareTo(ArrayHeapMinPQ.PriorityNode other) {
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
                return ((ArrayHeapMinPQ.PriorityNode) o).getItem().equals(getItem());
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
        ArrayHeapMinPQ<T> temp = new ArrayHeapMinPQ<>();
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
