package com.compomics.util.io.json.marshallers;

/**
 *
 * @author Kenneth Verheggen
 */
import com.compomics.util.experiment.biology.Atom;
import com.compomics.util.experiment.identification.identification_parameters.IdentificationAlgorithmParameter;
import com.compomics.util.io.json.JsonMarshaller;

/**
 * This class is a convenience class to have a DefaultJsonConverter with the
 * search parameter interfaces
 *
 * @author Kenneth Verheggen
 */
public class SearchParameterMarshaller extends JsonMarshaller {

    /**
     * Default constructor
     */
    public SearchParameterMarshaller() {
        super(IdentificationAlgorithmParameter.class, Atom.class);
    }

}
