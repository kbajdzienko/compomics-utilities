package com.compomics.util.experiment.biology.variants;

import com.compomics.util.experiment.biology.AminoAcid;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Matrix of amino acid substitutions. This class contains pre-implemented
 * matrices.
 *
 * @author Marc Vaudel
 */
public class AaSubstitutionMatrix {

    /**
     * The name of this substitution matrix.
     */
    private String name;
    /**
     * The description of the substitution matrix.
     */
    private String description;
    /**
     * Map of the possible amino acid substitutions: original aa &gt; substituted aa.
     */
    private final HashMap<Character, HashSet<Character>> substitutions = new HashMap<Character, HashSet<Character>>(26);
    /**
     * Reverse map of the possible amino acid substitution: substituted aa &gt;
     * original aa.
     */
    private final HashMap<Character, HashSet<Character>> reverseMap = new HashMap<Character, HashSet<Character>>(26);
    
    /**
     * Empty substitution matrix.
     */
    public static final AaSubstitutionMatrix noSubstitution = new AaSubstitutionMatrix("No substitution", "No substitution");
    /**
     * Substitution matrix allowing for a single base substitution.
     */
    public static final AaSubstitutionMatrix singleBaseSubstitution = singleBaseSubstitution();
    /**
     * Substitution matrix allowing for a single base transition variant.
     */
    public static final AaSubstitutionMatrix transitionsSingleBaseSubstitution = transitionsSingleBaseSubstitution();
    /**
     * Substitution matrix allowing for a single base transversion variant.
     */
    public static final AaSubstitutionMatrix transversalSingleBaseSubstitution = transversalSingleBaseSubstitution();
    /**
     * Substitution matrix grouping synonymous amino acids. Amino acids are grouped
     * according to their side chain properties: - Non-polar aliphatic groups:
     * {'G', 'A', 'V', 'L', 'M', 'I'} - Aromatic groups: {'F', 'Y', 'W'} - Polar
     * neutral groups: {'S', 'T', 'C', 'P', 'N', 'Q'} - Basic groups: {'K', 'R',
     * 'H'} - Acidic groups: {'D', 'E'}.
     */
    public static final AaSubstitutionMatrix synonymousVariant = synonymousVariant();
    /**
     * Returns the implemented default mutation matrices.
     */
    public static final AaSubstitutionMatrix[] defaultMutationMatrices = new AaSubstitutionMatrix[]{noSubstitution, singleBaseSubstitution, transitionsSingleBaseSubstitution, transversalSingleBaseSubstitution, synonymousVariant};

    /**
     * Constructor.
     *
     * @param name the name of this mutation matrix
     * @param description the description of the mutation matrix
     */
    public AaSubstitutionMatrix(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Adds a possible mutation.
     *
     * @param originalAa the original amino acid represented by its single
     * letter code
     * @param substitutionAa the substituted amino acid represented by its single letter
     * code
     */
    public void addMutation(Character originalAa, Character substitutionAa) {
        HashSet<Character> substitutedAas = substitutions.get(originalAa);
        if (substitutedAas == null) {
            substitutedAas = new HashSet<Character>();
            substitutions.put(originalAa, substitutedAas);
        }
        substitutedAas.add(substitutionAa);
        HashSet<Character> originalAas = reverseMap.get(originalAa);
        if (originalAas == null) {
            originalAas = new HashSet<Character>();
            reverseMap.put(substitutionAa, originalAas);
        }
        originalAas.add(originalAa);
    }
    
    /**
     * Returns the possible substituted amino acids for the given amino acid as a
     * list of their single letter code. Null if none found.
     *
     * @param originalAminoAcid the amino acid of interest
     *
     * @return the possible substituted amino acids
     */
    public HashSet<Character> getSubstitutedAminoAcids(Character originalAminoAcid) {
        return substitutions.get(originalAminoAcid);
    }

    /**
     * Returns the possible original amino acids for the given substituted amino
     * acid as a list of their single letter code. Null if none found.
     *
     * @param substitutedAminoAcid the mutated amino acid of interest
     *
     * @return the possible original amino acids for the given substituted amino
     * acid
     */
    public HashSet<Character> getOriginalAminoAcids(Character substitutedAminoAcid) {
        return reverseMap.get(substitutedAminoAcid);
    }

    /**
     * Returns the amino acids where a substitution has been registered.
     *
     * @return the amino acids where a substitution has been registered
     */
    public HashSet<Character> getOriginalAminoAcids() {
        return new HashSet<Character>(substitutions.keySet());
    }

    /**
     * Returns the possible substituted amino acids.
     *
     * @return the possible substituted amino acids
     */
    public HashSet<Character> getSubstitutionAminoAcids() {
        return new HashSet<Character>(substitutions.keySet());
    }

    /**
     * Adds the content of a mutation matrix in this matrix.
     *
     * @param otherMatrix the other matrix to add
     */
    public void add(AaSubstitutionMatrix otherMatrix) {
        for (Character originalAa : otherMatrix.getOriginalAminoAcids()) {
            for (Character mutatedAa : otherMatrix.getSubstitutedAminoAcids(originalAa)) {
                addMutation(originalAa, mutatedAa);
            }
        }
    }

    /**
     * Returns the substitution matrix allowing for a single base substitution.
     *
     * @return the substitution matrix allowing for a single base substitution
     */
    private static AaSubstitutionMatrix singleBaseSubstitution() {
        AaSubstitutionMatrix result = new AaSubstitutionMatrix("Single Base Substitution", "Single base substitutions");
        char[] bases = {'A', 'T', 'G', 'C'};
        for (char originalAa : AminoAcid.getAminoAcids()) {
            if (originalAa != 'X') {
                AminoAcid aminoAcid = AminoAcid.getAminoAcid(originalAa);
                for (String geneCode : aminoAcid.getStandardGeneticCode()) {
                    StringBuilder geneCodeStringBuilder = new StringBuilder(geneCode);
                    for (int i = 0; i < geneCode.length(); i++) {
                        char originalBase = geneCode.charAt(i);
                        for (char base : bases) {
                            geneCodeStringBuilder.setCharAt(i, base);
                            String newCode = geneCodeStringBuilder.toString();
                            AminoAcid mutatedAminoAcid = AminoAcid.getAminoAcidFromGeneticCode(newCode);
                            if (mutatedAminoAcid != null) {
                                char mutatedAa = mutatedAminoAcid.getSingleLetterCodeAsChar();
                                if (originalAa != mutatedAa) {
                                    result.addMutation(originalAa, mutatedAa);
                                }
                            }
                        }
                        geneCodeStringBuilder.setCharAt(i, originalBase);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the mutation matrix allowing for a single base transitions
     * variant.
     *
     * @return the mutation matrix allowing for a single base transitions
     * variant
     */
    private static AaSubstitutionMatrix transitionsSingleBaseSubstitution() {
        AaSubstitutionMatrix result = new AaSubstitutionMatrix("Single Base Transition", "Single base transitions substitutions.");
        char[] purines = {'A', 'G'}, pyrimidines = {'T', 'C'};
        for (char originalAa : AminoAcid.getAminoAcids()) {
            if (originalAa != 'X') {
                AminoAcid aminoAcid = AminoAcid.getAminoAcid(originalAa);
                for (String geneCode : aminoAcid.getStandardGeneticCode()) {
                    StringBuilder geneCodeStringBuilder = new StringBuilder(geneCode);
                    for (int i = 0; i < geneCode.length(); i++) {
                        char originalBase = geneCode.charAt(i);
                        char[] bases;
                        if (originalBase == purines[0] || originalBase == purines[1]) {
                            bases = purines;
                        } else if (originalBase == pyrimidines[0] || originalBase == pyrimidines[1]) {
                            bases = pyrimidines;
                        } else {
                            throw new IllegalArgumentException(originalBase + " not recognized for transitions substitution.");
                        }
                        for (char base : bases) {
                            geneCodeStringBuilder.setCharAt(i, base);
                            String newCode = geneCodeStringBuilder.toString();
                            AminoAcid mutatedAminoAcid = AminoAcid.getAminoAcidFromGeneticCode(newCode);
                            if (mutatedAminoAcid != null) {
                                char mutatedAa = mutatedAminoAcid.getSingleLetterCodeAsChar();
                                if (originalAa != mutatedAa) {
                                    result.addMutation(originalAa, mutatedAa);
                                }
                            }
                        }
                        geneCodeStringBuilder.setCharAt(i, originalBase);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the mutation matrix allowing for a single base transversion
     * variant.
     *
     * @return the mutation matrix allowing for a single base transversion
     * variant
     */
    private static AaSubstitutionMatrix transversalSingleBaseSubstitution() {
        AaSubstitutionMatrix result = new AaSubstitutionMatrix("Single Base Transversion", "Single base transversion substitutions.");
        char[] purines = {'A', 'G'}, pyrimidines = {'T', 'C'};
        for (char originalAa : AminoAcid.getAminoAcids()) {
            if (originalAa != 'X') {
                AminoAcid aminoAcid = AminoAcid.getAminoAcid(originalAa);
                for (String geneCode : aminoAcid.getStandardGeneticCode()) {
                    StringBuilder geneCodeStringBuilder = new StringBuilder(geneCode);
                    for (int i = 0; i < geneCode.length(); i++) {
                        char originalBase = geneCode.charAt(i);
                        char[] bases;
                        if (originalBase == purines[0] || originalBase == purines[1]) {
                            bases = pyrimidines;
                        } else if (originalBase == pyrimidines[0] || originalBase == pyrimidines[1]) {
                            bases = purines;
                        } else {
                            throw new IllegalArgumentException(originalBase + " not recognized for transversion substitutions.");
                        }
                        for (char base : bases) {
                            geneCodeStringBuilder.setCharAt(i, base);
                            String newCode = geneCodeStringBuilder.toString();
                            AminoAcid mutatedAminoAcid = AminoAcid.getAminoAcidFromGeneticCode(newCode);
                            if (mutatedAminoAcid != null) {
                                char mutatedAa = mutatedAminoAcid.getSingleLetterCodeAsChar();
                                if (originalAa != mutatedAa) {
                                    result.addMutation(originalAa, mutatedAa);
                                }
                            }
                        }
                        geneCodeStringBuilder.setCharAt(i, originalBase);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns a substitution matrix grouping synonymous amino acids. Amino acids
     * are grouped according to their side chain properties: - Non-polar
     * aliphatic groups: {'G', 'A', 'V', 'L', 'M', 'I'} - Aromatic groups: {'F',
     * 'Y', 'W'} - Polar neutral groups: {'S', 'T', 'C', 'P', 'N', 'Q'} - Basic
     * groups: {'K', 'R', 'H'} - Acidic groups: {'D', 'E'}.
     *
     * @return a substitution matrix grouping synonymous amino acids
     */
    private static AaSubstitutionMatrix synonymousVariant() {
        AaSubstitutionMatrix result = new AaSubstitutionMatrix("Synonymous Variant", "Variants keeping amino acid properties.");
        char[] nonPolarAliphatic = new char[]{'G', 'A', 'V', 'L', 'M', 'I'};
        for (char originalAminoAcid : nonPolarAliphatic) {
            for (char mutatedAminoAcid : nonPolarAliphatic) {
                if (originalAminoAcid != mutatedAminoAcid) {
                    result.addMutation(originalAminoAcid, mutatedAminoAcid);
                }
            }
            result.addMutation('J', originalAminoAcid);
        }
        char[] aromatic = new char[]{'F', 'Y', 'W'};
        for (char originalAminoAcid : aromatic) {
            for (char mutatedAminoAcid : aromatic) {
                if (originalAminoAcid != mutatedAminoAcid) {
                    result.addMutation(originalAminoAcid, mutatedAminoAcid);
                }
            }
        }
        char[] polarNeutral = new char[]{'S', 'T', 'C', 'P', 'N', 'Q'};
        for (char originalAminoAcid : polarNeutral) {
            for (char mutatedAminoAcid : polarNeutral) {
                if (originalAminoAcid != mutatedAminoAcid) {
                    result.addMutation(originalAminoAcid, mutatedAminoAcid);
                }
            }
            result.addMutation('B', originalAminoAcid);
            result.addMutation('Z', originalAminoAcid);
        }
        char[] basic = new char[]{'K', 'R', 'H'};
        for (char originalAminoAcid : basic) {
            for (char mutatedAminoAcid : basic) {
                if (originalAminoAcid != mutatedAminoAcid) {
                    result.addMutation(originalAminoAcid, mutatedAminoAcid);
                }
            }
        }
        char[] acidic = new char[]{'D', 'E'};
        for (char originalAminoAcid : acidic) {
            for (char mutatedAminoAcid : acidic) {
                if (originalAminoAcid != mutatedAminoAcid) {
                    result.addMutation(originalAminoAcid, mutatedAminoAcid);
                }
            }
            result.addMutation('B', originalAminoAcid);
            result.addMutation('Z', originalAminoAcid);
        }
        return result;
    }

    /**
     * Returns the name of this mutation matrix.
     *
     * @return the name of this mutation matrix
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this mutation matrix.
     *
     * @param name the name of this mutation matrix
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of this mutation matrix.
     *
     * @return the description of this mutation matrix
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this mutation matrix.
     *
     * @param description the description of this mutation matrix
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Indicates whether the given AaSubstitutionMatrix is the same as this one.
     *
     * @param aaSubstitutionMatrix the mutation matrix
     *
     * @return a boolean indicating whether the given AaSubstitutionMatrix is
     * the same as this one
     */
    public boolean isSameAs(AaSubstitutionMatrix aaSubstitutionMatrix) {
        if (this.equals(aaSubstitutionMatrix)) {
            return true;
        }
        if (!name.equals(aaSubstitutionMatrix.getName())) {
            return false;
        }
        if (!description.equals(aaSubstitutionMatrix.getDescription())) {
            return false;
        }
        for (Character aa : substitutions.keySet()) {
            HashSet<Character> aaMutations = substitutions.get(aa);
            HashSet<Character> otherMutations = aaSubstitutionMatrix.getSubstitutedAminoAcids(aa);
            if (otherMutations == null || aaMutations.size() != otherMutations.size()) {
                return false;
            }
            for (Character mutatedAa : aaMutations) {
                if (!otherMutations.contains(mutatedAa)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
