package edu.umass.cs.ciir.controversy.data;

import org.lemurproject.galago.core.btree.simple.DiskMapBuilder;
import org.lemurproject.galago.core.btree.simple.DiskMapReader;
import org.lemurproject.galago.core.btree.simple.DiskMapSortedBuilder;
import org.lemurproject.galago.tupleflow.Utility;
import org.lemurproject.galago.utility.ByteUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * Created by mhjang on 2/6/16.
 */
public class BuildIndex {
    public static void main(String[] args) throws IOException {
        BuildIndex bi = new BuildIndex();
        bi.buildIndex();
 //       bi.testIndex();
    }

    public static void testIndex() throws IOException {
        DiskMapReader reader = new DiskMapReader(DataPath.MSCORE);
        System.out.println(reader.containsKey(ByteUtil.fromString("abortion")));

        System.out.println(Utility.toDouble((reader.get(ByteUtil.fromString("abortion")))));

    }

    public void buildIndex() throws IOException {
            /******************************
             * building a B-tree index
             ******************************/

            DiskMapBuilder dmb = new DiskMapBuilder("/home/mhjang/controversy_Data/datasets/resources/CScore_pair.index");
            String fileName = "/home/mhjang/controversy_Data/datasets/resources/pairbasednetwork/revised.CSCORE";
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String line = null;

            while((line = br.readLine()) != null) {
                try{
                    StringTokenizer st = new StringTokenizer(line, "\t");
                    String title = st.nextToken().toLowerCase();
                    byte[] score = Utility.fromDouble(Double.parseDouble(st.nextToken()));
                    byte[] ngram = ByteUtil.fromString(title);
                    dmb.put(ngram, score);
                } catch(NumberFormatException e) {
                    System.out.println(line);
                    e.printStackTrace();
                }
            }
            dmb.close();

            //   DiskMapReader dmr = DiskMapReader.fromMap("ngram_index/1gms0", map);

            //  dmb.close();

            /***
             * loading the index file
             */
            //    File file = new File("ngram_index/1gms");

            /**
             * Build an on-disk map using galago
             */
   /*     DiskMapReader mapReader = new DiskMapReader(file.getAbsolutePath());
        System.out.println(Runtime.getRuntime().maxMemory() / (1024 * 1024 * 1024));
        /*
         * pull keys
         */
  /*      byte[] data = mapReader.get(Utility.fromString("dynamic"));
        if(data != null) {
            String count = Utility.toString(data);
            System.out.println("count: "+ count);
        }

        // java using object equality is dumb
        // assertTrue(memKeys.contains(key)) will fail because it does pointer comparisons...
*/
        }
    }

