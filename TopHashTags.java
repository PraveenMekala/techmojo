import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopHashTags {
    public static void main(String args[]) throws IOException {
        String fileName = "tweets.txt";
        int topCount = 3;
        Map<String, Integer> hashTagCountMap = getHashTagVsCountMap(fileName);
        List<String> topTagsList = getTopKHashTags(hashTagCountMap, topCount);
        Collections.reverse(topTagsList);
        System.out.println(topTagsList);
    }

    public static Map<String, Integer> getHashTagVsCountMap(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader( new File(fileName)));
        Map<String, Integer> frequencyMap = new HashMap<>();
        String tweet;
        while ((tweet = br.readLine()) != null) {
            Pattern pattern = Pattern.compile("#(?<hashTag>[a-zA-Z0-9]+\\s*)");
            Matcher matcher = pattern.matcher(tweet);
            while (matcher.find()) {
                String hashTag = matcher.group("hashTag");
                hashTag = hashTag.trim();
                if (frequencyMap.containsKey(hashTag)) {
                    frequencyMap.put(hashTag, frequencyMap.get(hashTag) + 1);
                } else {
                    frequencyMap.put(hashTag, 1);
                }
            }
        }
        return frequencyMap;
    }

    public static List<String> getTopKHashTags(Map<String, Integer> hashTagCountMap, int n) {
        PriorityQueue<HashTagFreq> topKHeap = new PriorityQueue<>(n);
        for (Map.Entry<String, Integer> entry : hashTagCountMap.entrySet()) {
            HashTagFreq hashTagFreq = new HashTagFreq(entry.getKey(), entry.getValue());
            if (topKHeap.size() < n) {
                topKHeap.add(hashTagFreq);
            } else if (entry.getValue() > topKHeap.peek().frequency) {
                topKHeap.poll();
                topKHeap.add(hashTagFreq);
            }
        }

        List<String> topTagsList = new ArrayList<>();
        while (topKHeap.size() > 0) {
            topTagsList.add(topKHeap.poll().hashTag);
        }
        return topTagsList;
    }
}

class HashTagFreq implements Comparable<HashTagFreq> {
    String hashTag;
    Integer frequency;

    public HashTagFreq(String hashTag, int frequency) {
        this.hashTag = hashTag;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(HashTagFreq hashTagFreq) {
        if(this.frequency > hashTagFreq.frequency)
            return 1;
        else if (this.frequency < hashTagFreq.frequency)
            return -1;
        else
            return 0;
        //return this.frequency.compareTo(hashTagFreq.frequency);
    }
}