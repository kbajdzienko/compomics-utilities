package com.compomics.util.experiment.biology.aminoacids;

import com.compomics.util.experiment.biology.AminoAcid;

/**
 * Asn or Asp: Asx (Mascot).
 *
 * @author Harald Barsnes
 */
public class B extends AminoAcid {

    /**
     * Serial number for backward compatibility.
     */
    static final long serialVersionUID = -584166511231722270L;

    /**
     * Constructor.
     */
    public B() {
        singleLetterCode = "B";
        threeLetterCode = "Asx";
        name = "B_Mascot";
        averageMass = 114.595;
        monoisotopicMass = 114.534935;
    }

    @Override
    public char[] getSubAminoAcids() {
        return new char[]{'N', 'D'};
    }

    @Override
    public char[] getCombinations() {
        return new char[]{'X'};
    }
}
