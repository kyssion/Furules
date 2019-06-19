package com.kyssion.galaxy.script.translater.analysis;

import com.kyssion.galaxy.script.translater.data.workKeyData.LexicalAnalysisData;
import com.kyssion.galaxy.script.translater.data.workKeyData.StopNoteMap;
import com.kyssion.galaxy.script.translater.symbol.SymbolType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalysis {

    /**
     * 构建词法表
     * @param reader
     * @return
     * @throws IOException
     */
    public static List<List<LexicalAnalysisData>> analysis(BufferedReader reader) throws IOException {
        List<List<LexicalAnalysisData>> mapperAnalysisList =
                new ArrayList<>();
        String lineTxt;
        StringBuilder note = new StringBuilder();
        List<LexicalAnalysisData> analysisDataList = new ArrayList<>();
        while ((lineTxt = reader.readLine()) != null) {
            if (analysisDataList.size() > 0 && lineTxt.startsWith("namespace")) {
                mapperAnalysisList.add(analysisDataList);
                analysisDataList = new ArrayList<>();
            }
            for (int a = 0; a < lineTxt.length(); a++) {
                if (StopNoteMap.isBeforeNote(lineTxt.charAt(a)) ||
                        StopNoteMap.isEndNote(lineTxt.charAt(a))) {
                    String value = note.toString();
                    SymbolType symbolType = SymbolType.getTypeByKeyWord(value);
                    analysisDataList.add(LexicalAnalysisData.create(value, a, symbolType));
                } else {
                    note.append(lineTxt.charAt(a));
                }
            }
        }
        return mapperAnalysisList;
    }
}
