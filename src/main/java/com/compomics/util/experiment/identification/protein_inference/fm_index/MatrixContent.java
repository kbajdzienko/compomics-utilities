package com.compomics.util.experiment.identification.protein_inference.fm_index;

import com.compomics.util.experiment.identification.matches.ModificationMatch;
import java.util.ArrayList;

/**
 * Element for the matrix necessary in pattern search of the FMIndex.
 *
 * @author Dominik Kopczynski
 */
public class MatrixContent {

    /**
     * Left Index.
     */
    public int left;
    
    /**
     * Right Index.
     */
    public int right;
    
    /**
     * Character which was chosen.
     */
    public char character;
    
    
    /**
     * Index of the originating entry of a particular cell with the pattern
     * searching matrix.
     */
    public MatrixContent previousContent;
    
    /**
     * Current mass.
     */
    public double mass;
    
    /**
     * Current peptide sequence.
     */
    public String peptideSequence;
    
    /**
     * Current peptide sequence length.
     */
    public int length;
    
    /**
     * Current number of contained Xs.
     */
    public int numX;    
    
    /**
     * Index to the modifications list.
     */
    public int modificationNum;
    
    /**
     * List of all modifictations
     */
    public ArrayList<ModificationMatch> modifications;
    
    /**
     * Constructor
     * 
     * @param left
     * @param right
     * @param character
     * @param previousContent
     * @param mass
     * @param peptideSequence
     * @param length
     * @param numX
     * @param modificationNum
     * @param modifications 
     */
    public MatrixContent(int left, int right, char character, MatrixContent previousContent, double mass, String peptideSequence, int length, int numX, int modificationNum, ArrayList<ModificationMatch> modifications) {
        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = mass;
        this.peptideSequence = peptideSequence;
        this.length = length;
        this.numX = numX;
        this.modificationNum = modificationNum;
        this.modifications = modifications;
    }
    
    /**
     * Copy constructor
     * @param foreign 
     */
    public MatrixContent(MatrixContent foreign){
        this.left = foreign.left;
        this.right = foreign.right;
        this.character = foreign.character;
        this.previousContent = foreign.previousContent;
        this.mass = foreign.mass;
        this.peptideSequence = foreign.peptideSequence;
        this.length = foreign.length;
        this.numX = foreign.numX;
        this.modificationNum = foreign.modificationNum;
        this.modifications = foreign.modifications;
    }
    
    /**
     * Constructor 
     * 
     * @param left
     * @param right
     * @param character
     * @param previousContent
     * @param numX 
     */
    public MatrixContent(int left, int right, char character, MatrixContent previousContent, int numX) {
        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = -1;
        this.peptideSequence = null;
        this.length = 0;
        this.numX = numX;
        this.modificationNum = -1;
        this.modifications = null;
    }
}
