package cmp3001;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;



public class Test {
	public static void main(String [] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		ReadWriteLock RW = new ReadWriteLock();
		
		executorService.execute(new Writer(RW,1));
		executorService.execute(new Writer(RW,2));
		executorService.execute(new Writer(RW,3));
		executorService.execute(new Writer(RW,4));
		
		executorService.execute(new Reader(RW,1));
		executorService.execute(new Reader(RW,2));
		executorService.execute(new Reader(RW,3));
		executorService.execute(new Reader(RW,4));
		}
}


class ReadWriteLock{
	private Semaphore forReadS;
	private Semaphore forWriteS;
	private int countOfReader; 
	

	public ReadWriteLock() 
	{
		    forReadS = new Semaphore(1);
		    forWriteS = new Semaphore(1);
			countOfReader=0;
	}
	
	public void readLock(int readerNumber)   {
		try {
			forReadS.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}	 
	++countOfReader;
	if (countOfReader == 1)
	{
		try
		{
			forWriteS.acquire();
		}
		catch (InterruptedException e) {}
	}

	System.out.println(readerNumber + " reading, count of readers = " + countOfReader);
	
	forReadS.release();
	
	}
	public void writeLock(int writerNumber) {
		try {
			forWriteS.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		System.out.println( writerNumber + " writing");
		
	}
	public void readUnLock(int readerNumber) {
		try {
			forReadS.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
	--countOfReader;
	if (countOfReader == 0)
	{
		forWriteS.release();
	}  
	System.out.println(readerNumber + " is finish reading, count of reader = " + countOfReader);	
	
	forReadS.release(); 
	}
	public void writeUnLock(int writerNumber) {
		
		System.out.println( writerNumber + " is done writing.");
		forWriteS.release();
	}

}




class Writer implements Runnable
{
   private ReadWriteLock RW_lock;
   private int writerNumber;

    public Writer(ReadWriteLock rw, int writerNumber) {
    	RW_lock = rw;
    	this.writerNumber= writerNumber;
   }

    public void run() {
      while (true){
    	  RW_lock.writeLock(writerNumber);
    	
    	  RW_lock.writeUnLock(writerNumber);
       
      }
   }


}



class Reader implements Runnable
{
   private ReadWriteLock RW_lock;
   private int readerNumber;

   public Reader(ReadWriteLock rw , int readerNumber) {
    	RW_lock = rw;
    	this.readerNumber= readerNumber;
   }
    public void run() {
      while (true){ 	    	  
    	  RW_lock.readLock(readerNumber);
    	 
    	  
    	  RW_lock.readUnLock(readerNumber);
       
      }
   }


}


