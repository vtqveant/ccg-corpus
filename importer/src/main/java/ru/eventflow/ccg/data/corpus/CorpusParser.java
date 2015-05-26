package ru.eventflow.ccg.data.corpus;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Замечания о формате экспорта подкорпуса со снятой неоднозначностью.
 *
 *   1) XSD для основного дампа корпуса (без снятия неоднозначности) не сопадает с тем, что есть
 *      в дампе подкорпуса с снятой неоднозначностью (в частности, //tfr/@rev_id нет в XSD)
 *   2) в будущем может вызвать проблемы отсутствие в дампе информации о версии словаря, по
 *      которому берутся граммемы для словоформ; чтобы подстраховаться, приходится вытаскивать и
 *      сами формы и граммемы, хотя можно было бы просто указать в токене id формы в словаре, и брать
 *      граммемы из словаря
 *   3) в дампе попадаются токены с id леммы 0, что в действительности означает Out of Vocabulary,
 *      напр., у всей пунктуации или орфографических ошибок (см. sentence id 25149). Т.е. хотя id у леммы проставлен, он как минимум не уникален,
 *      т.е. нарушение семантики id)
 */
public class CorpusParser {

    private CorpusDataCollector collector;

    public CorpusParser(CorpusDataCollector collector) {
        this.collector = collector;
    }

    public void process(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();

        CorpusHandler handler = new CorpusHandler(xmlReader, collector);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(new InputSource(in));
    }

}
