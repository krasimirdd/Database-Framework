package com.kdd.productshop.util;

import java.io.*;

public class FileIOUtilImpl implements FileIOUtil {
    @Override
    public String readFile(String filePath) throws IOException {
        File file = new File(filePath);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file)
                ));

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }
}
