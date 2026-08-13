package gh.edu.ug.cs.ugmaintenance.datastructures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

import gh.edu.ug.cs.ugmaintenance.datastructures.heap.Heap;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

public class HeapTest {

    private Heap heap;

    @Before
    public void setup() {
         heap = new Heap();
    }

    @Test 
    public void higherUrgencyRequestComesFirst() {
        ServiceRequest lowUrgency = new ServiceRequest(
            1, 1, 1, 1,
            "Low urgency request",
            "test request",
            2,
            null,
            LocalDateTime.now(),
            null
        );

        ServiceRequest  highUrgency = new ServiceRequest(
            2, 1, 1, 1, 
            "High urgency request",
            "Test request",
            5,
            null,
             LocalDateTime.now(),
            null
        );

        heap.insert(lowUrgency);
        heap.insert(highUrgency);

        ServiceRequest result = heap.extractMax();

        assertEquals(highUrgency, result);
        
       }
    
    @Test
    public void maxUrgencyGetsBonusPriority() {
        ServiceRequest normalrequest = new ServiceRequest(
            3, 1, 1, 1,
            "Normalrequest",
            "Test request",
            4,
            null,
            LocalDateTime.now(),
            null
        );

        ServiceRequest criticalRequest = new ServiceRequest(
            4, 1, 1, 1,
            "Critical request",
            "test request",
            5,
            null,
            LocalDateTime.now(),
            null
        );

        heap.insert(normalrequest);
        heap.insert(criticalRequest);

        ServiceRequest result = heap.extractMax();

        assertEquals(criticalRequest, result);


    }


    
}