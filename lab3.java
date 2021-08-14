import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class lab3 {
    private ArrayList<Job> jobs;
    private int finishTime;
    private boolean hasCycle;// To check if there is a cycle in the graph
    private boolean allVisited;//Check if all vertices have been visited to avoid the same running

    public lab3() {
        this.jobs = new ArrayList<>();
        this.hasCycle = false;
        this.allVisited = false;
        this.finishTime = 0;
    }
    public Job insert(int time) {
        Job job = new Job(time);
        this.jobs.add(job);
        this.allVisited = false;
        return job;
    }

    public Job get(int index) {
        return jobs.get(index);
    }

    public int finish() {
        int checker = 0;
        if(this.hasCycle && this.allVisited) return -1;
        if(this.allVisited) return this.finishTime;

        Queue<Job> queue = new LinkedList<>();
        //store all vertices that have no inDegree into queue
        for (Job tempJob : jobs) {
            tempJob.tempInDegree = tempJob.inDegree;
            if (tempJob.inDegree == 0) {
                queue.add(tempJob);
            }
        }
        // go through the queue
        while (!queue.isEmpty()) {
            checker++;
            Job currentJob = queue.poll();
            currentJob.maxTime(queue); //find the max total time for each job
            //The variable finishTime will get the largest maxTotalTime in all jobs;
            this.finishTime = this.finishTime < currentJob.maxTotalTime ? currentJob.maxTotalTime : this.finishTime;
        }
        this.allVisited = true; // all vertices have been visited
        if( checker != jobs.size() ){
            this.hasCycle = true;// there is a cycle
            return -1;
        }
        return this.finishTime;
    }

    public class Job {

        private int time;
        private int inDegree;
        //A instant variable that will temporarily get the value from the inDegree variable
        //in order to manipulate each job's inDegree without changing them
        private int tempInDegree;
        //This instant variable will get the max total time in the process of relaxing each edge
        private int maxTotalTime;
        private ArrayList<Job> prerequisiteBy;

        private Job(int time) {
            this.time = time;
            this.inDegree = 0;
            this.tempInDegree = 0;
            this.maxTotalTime = time;
            this.prerequisiteBy = new ArrayList<>();
        }

        public void requires(Job job) {
            job.prerequisiteBy.add(this);
            this.inDegree++;
            lab3.this.allVisited = false;
        }

        public int start() {
            if (this.inDegree == 0) return 0;
            finish();
            if (this.tempInDegree == 0)
                return this.maxTotalTime - this.time;

            return -1;
        }
        // This method will calculate the final finish and start time
        private void maxTime(Queue<Job> queue) {
            for (Job job : this.prerequisiteBy) {
                // relax all edges
                if (job.maxTotalTime < this.maxTotalTime + job.time) {
                    job.maxTotalTime = this.maxTotalTime + job.time;
                }
                if (--job.tempInDegree == 0) {
                    queue.add(job);
                }
            }
        }
    }
}
