/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.commons.compress.harmony.pack200;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A group of metadata (annotation) bands, such as class_RVA_bands, method_AD_bands etc.
 */
public class MetadataBandGroup extends BandSet {

    public static final int CONTEXT_CLASS = 0;
    public static final int CONTEXT_FIELD = 1;
    public static final int CONTEXT_METHOD = 2;

    private final String type;
    private int numBackwardsCalls = 0;

    public IntList param_NB = new IntList(); // TODO: Lazy instantiation?
    public IntList anno_N = new IntList();
    public List type_RS = new ArrayList();
    public IntList pair_N = new IntList();
    public List name_RU = new ArrayList();
    public List T = new ArrayList();
    public List caseI_KI = new ArrayList();
    public List caseD_KD = new ArrayList();
    public List caseF_KF = new ArrayList();
    public List caseJ_KJ = new ArrayList();
    public List casec_RS = new ArrayList();
    public List caseet_RS = new ArrayList();
    public List caseec_RU = new ArrayList();
    public List cases_RU = new ArrayList();
    public IntList casearray_N = new IntList();
    public List nesttype_RS = new ArrayList();
    public IntList nestpair_N = new IntList();
    public List nestname_RU = new ArrayList();

    private final CpBands cpBands;
    private final int context;

    /**
     * Constructs a new MetadataBandGroup
     *
     * @param type must be either AD, RVA, RIA, RVPA or RIPA.
     * @param context <code>CONTEXT_CLASS</code>, <code>CONTEXT_METHOD</code> or <code>CONTEXT_FIELD</code>
     * @param cpBands constant pool bands
     * @param segmentHeader segment header
     * @param effort packing effort
     */
    public MetadataBandGroup(final String type, final int context, final CpBands cpBands,
        final SegmentHeader segmentHeader, final int effort) {
        super(effort, segmentHeader);
        this.type = type;
        this.cpBands = cpBands;
        this.context = context;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.commons.compress.harmony.pack200.BandSet#pack(java.io.OutputStream)
     */
    @Override
    public void pack(final OutputStream out) throws IOException, Pack200Exception {
        PackingUtils.log("Writing metadata band group...");
        if (hasContent()) {
            String contextStr;
            if (context == CONTEXT_CLASS) {
                contextStr = "Class";
            } else if (context == CONTEXT_FIELD) {
                contextStr = "Field";
            } else {
                contextStr = "Method";
            }
            byte[] encodedBand = null;
            if (!type.equals("AD")) {
                if (type.indexOf('P') != -1) {
                    // Parameter annotation so we need to transmit param_NB
                    encodedBand = encodeBandInt(contextStr + "_" + type + " param_NB", param_NB.toArray(), Codec.BYTE1);
                    out.write(encodedBand);
                    PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type
                        + " anno_N[" + param_NB.size() + "]");
                }
                encodedBand = encodeBandInt(contextStr + "_" + type + " anno_N", anno_N.toArray(), Codec.UNSIGNED5);
                out.write(encodedBand);
                PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " anno_N["
                    + anno_N.size() + "]");

                encodedBand = encodeBandInt(contextStr + "_" + type + " type_RS", cpEntryListToArray(type_RS),
                    Codec.UNSIGNED5);
                out.write(encodedBand);
                PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " type_RS["
                    + type_RS.size() + "]");

                encodedBand = encodeBandInt(contextStr + "_" + type + " pair_N", pair_N.toArray(), Codec.UNSIGNED5);
                out.write(encodedBand);
                PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " pair_N["
                    + pair_N.size() + "]");

                encodedBand = encodeBandInt(contextStr + "_" + type + " name_RU", cpEntryListToArray(name_RU),
                    Codec.UNSIGNED5);
                out.write(encodedBand);
                PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " name_RU["
                    + name_RU.size() + "]");
            }
            encodedBand = encodeBandInt(contextStr + "_" + type + " T", tagListToArray(T), Codec.BYTE1);
            out.write(encodedBand);
            PackingUtils
                .log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " T[" + T.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " caseI_KI", cpEntryListToArray(caseI_KI),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " caseI_KI["
                + caseI_KI.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " caseD_KD", cpEntryListToArray(caseD_KD),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " caseD_KD["
                + caseD_KD.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " caseF_KF", cpEntryListToArray(caseF_KF),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " caseF_KF["
                + caseF_KF.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " caseJ_KJ", cpEntryListToArray(caseJ_KJ),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " caseJ_KJ["
                + caseJ_KJ.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " casec_RS", cpEntryListToArray(casec_RS),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " casec_RS["
                + casec_RS.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " caseet_RS", cpEntryListToArray(caseet_RS),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " caseet_RS["
                + caseet_RS.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " caseec_RU", cpEntryListToArray(caseec_RU),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " caseec_RU["
                + caseec_RU.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " cases_RU", cpEntryListToArray(cases_RU),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " cases_RU["
                + cases_RU.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " casearray_N", casearray_N.toArray(),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " casearray_N["
                + casearray_N.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " nesttype_RS", cpEntryListToArray(nesttype_RS),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " nesttype_RS["
                + nesttype_RS.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " nestpair_N", nestpair_N.toArray(), Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " nestpair_N["
                + nestpair_N.size() + "]");

            encodedBand = encodeBandInt(contextStr + "_" + type + " nestname_RU", cpEntryListToArray(nestname_RU),
                Codec.UNSIGNED5);
            out.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + type + " nestname_RU["
                + nestname_RU.size() + "]");
        }
    }

    private int[] tagListToArray(final List t2) {
        final int[] ints = new int[t2.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = ((String) t2.get(i)).charAt(0);
        }
        return ints;
    }

    /**
     * Add an annotation to this set of bands.
     *
     * @param numParams TODO
     * @param annoN TODO
     * @param pairN TODO
     * @param typeRS TODO
     * @param nameRU TODO
     * @param t TODO
     * @param values TODO
     * @param caseArrayN TODO
     * @param nestTypeRS TODO
     * @param nestNameRU TODO
     * @param nestPairN TODO
     */
    public void addParameterAnnotation(final int numParams, final int[] annoN, final IntList pairN, final List typeRS,
        final List nameRU, final List t, final List values, final List caseArrayN, final List nestTypeRS,
        final List nestNameRU, final List nestPairN) {
        param_NB.add(numParams);
        for (int element : annoN) {
            anno_N.add(element);
        }
        pair_N.addAll(pairN);
        for (Object element : typeRS) {
            final String desc = (String) element;
            type_RS.add(cpBands.getCPSignature(desc));
        }
        for (Object element : nameRU) {
            final String name = (String) element;
            name_RU.add(cpBands.getCPUtf8(name));
        }
        final Iterator valuesIterator = values.iterator();
        for (Object element : t) {
            final String tag = (String) element;
            T.add(tag);
            if (tag.equals("B") || tag.equals("C") || tag.equals("I") || tag.equals("S") || tag.equals("Z")) {
                final Integer value = (Integer) valuesIterator.next();
                caseI_KI.add(cpBands.getConstant(value));
            } else if (tag.equals("D")) {
                final Double value = (Double) valuesIterator.next();
                caseD_KD.add(cpBands.getConstant(value));
            } else if (tag.equals("F")) {
                final Float value = (Float) valuesIterator.next();
                caseF_KF.add(cpBands.getConstant(value));
            } else if (tag.equals("J")) {
                final Long value = (Long) valuesIterator.next();
                caseJ_KJ.add(cpBands.getConstant(value));
            } else if (tag.equals("c")) {
                final String value = (String) valuesIterator.next();
                casec_RS.add(cpBands.getCPSignature(value));
            } else if (tag.equals("e")) {
                final String value = (String) valuesIterator.next();
                final String value2 = (String) valuesIterator.next();
                caseet_RS.add(cpBands.getCPSignature(value));
                caseec_RU.add(cpBands.getCPUtf8(value2));
            } else if (tag.equals("s")) {
                final String value = (String) valuesIterator.next();
                cases_RU.add(cpBands.getCPUtf8(value));
            }
            // do nothing here for [ or @ (handled below)
        }
        for (Object element : caseArrayN) {
            final int arraySize = ((Integer) element).intValue();
            casearray_N.add(arraySize);
            numBackwardsCalls += arraySize;
        }
        for (Object element : nestTypeRS) {
            final String type = (String) element;
            nesttype_RS.add(cpBands.getCPSignature(type));
        }
        for (Object element : nestNameRU) {
            final String name = (String) element;
            nestname_RU.add(cpBands.getCPUtf8(name));
        }
        for (Object element : nestPairN) {
            final Integer numPairs = (Integer) element;
            nestpair_N.add(numPairs.intValue());
            numBackwardsCalls += numPairs.intValue();
        }
    }

    /**
     * Add an annotation to this set of bands
     *
     * @param desc TODO
     * @param nameRU TODO
     * @param t TODO
     * @param values TODO
     * @param caseArrayN TODO
     * @param nestTypeRS TODO
     * @param nestNameRU TODO
     * @param nestPairN TODO
     */
    public void addAnnotation(final String desc, final List nameRU, final List t, final List values,
        final List caseArrayN, final List nestTypeRS, final List nestNameRU, final List nestPairN) {
        type_RS.add(cpBands.getCPSignature(desc));
        pair_N.add(nameRU.size());

        for (Object element : nameRU) {
            final String name = (String) element;
            name_RU.add(cpBands.getCPUtf8(name));
        }

        final Iterator valuesIterator = values.iterator();
        for (Object element : t) {
            final String tag = (String) element;
            T.add(tag);
            if (tag.equals("B") || tag.equals("C") || tag.equals("I") || tag.equals("S") || tag.equals("Z")) {
                final Integer value = (Integer) valuesIterator.next();
                caseI_KI.add(cpBands.getConstant(value));
            } else if (tag.equals("D")) {
                final Double value = (Double) valuesIterator.next();
                caseD_KD.add(cpBands.getConstant(value));
            } else if (tag.equals("F")) {
                final Float value = (Float) valuesIterator.next();
                caseF_KF.add(cpBands.getConstant(value));
            } else if (tag.equals("J")) {
                final Long value = (Long) valuesIterator.next();
                caseJ_KJ.add(cpBands.getConstant(value));
            } else if (tag.equals("c")) {
                final String value = (String) valuesIterator.next();
                casec_RS.add(cpBands.getCPSignature(value));
            } else if (tag.equals("e")) {
                final String value = (String) valuesIterator.next();
                final String value2 = (String) valuesIterator.next();
                caseet_RS.add(cpBands.getCPSignature(value));
                caseec_RU.add(cpBands.getCPUtf8(value2));
            } else if (tag.equals("s")) {
                final String value = (String) valuesIterator.next();
                cases_RU.add(cpBands.getCPUtf8(value));
            }
            // do nothing here for [ or @ (handled below)
        }
        for (Object element : caseArrayN) {
            final int arraySize = ((Integer) element).intValue();
            casearray_N.add(arraySize);
            numBackwardsCalls += arraySize;
        }
        for (Object element : nestTypeRS) {
            final String type = (String) element;
            nesttype_RS.add(cpBands.getCPSignature(type));
        }
        for (Object element : nestNameRU) {
            final String name = (String) element;
            nestname_RU.add(cpBands.getCPUtf8(name));
        }
        for (Object element : nestPairN) {
            final Integer numPairs = (Integer) element;
            nestpair_N.add(numPairs.intValue());
            numBackwardsCalls += numPairs.intValue();
        }
    }

    /**
     * Returns true if any annotations have been added to this set of bands.
     *
     * @return true if any annotations have been added to this set of bands.
     */
    public boolean hasContent() {
        return type_RS.size() > 0;
    }

    public int numBackwardsCalls() {
        return numBackwardsCalls;
    }

    public void incrementAnnoN() {
        anno_N.increment(anno_N.size() - 1);
    }

    public void newEntryInAnnoN() {
        anno_N.add(1);
    }

    /**
     * Remove the latest annotation that was added to this group
     */
    public void removeLatest() {
        final int latest = anno_N.remove(anno_N.size() - 1);
        for (int i = 0; i < latest; i++) {
            type_RS.remove(type_RS.size() - 1);
            final int pairs = pair_N.remove(pair_N.size() - 1);
            for (int j = 0; j < pairs; j++) {
                removeOnePair();
            }
        }
    }

    /*
     * Convenience method for removeLatest
     */
    private void removeOnePair() {
        final String tag = (String) T.remove(T.size() - 1);
        if (tag.equals("B") || tag.equals("C") || tag.equals("I") || tag.equals("S") || tag.equals("Z")) {
            caseI_KI.remove(caseI_KI.size() - 1);
        } else if (tag.equals("D")) {
            caseD_KD.remove(caseD_KD.size() - 1);
        } else if (tag.equals("F")) {
            caseF_KF.remove(caseF_KF.size() - 1);
        } else if (tag.equals("J")) {
            caseJ_KJ.remove(caseJ_KJ.size() - 1);
        } else if (tag.equals("C")) {
            casec_RS.remove(casec_RS.size() - 1);
        } else if (tag.equals("e")) {
            caseet_RS.remove(caseet_RS.size() - 1);
            caseec_RU.remove(caseet_RS.size() - 1);
        } else if (tag.equals("s")) {
            cases_RU.remove(cases_RU.size() - 1);
        } else if (tag.equals("[")) {
            final int arraySize = casearray_N.remove(casearray_N.size() - 1);
            numBackwardsCalls -= arraySize;
            for (int k = 0; k < arraySize; k++) {
                removeOnePair();
            }
        } else if (tag.equals("@")) {
            nesttype_RS.remove(nesttype_RS.size() - 1);
            final int numPairs = nestpair_N.remove(nestpair_N.size() - 1);
            numBackwardsCalls -= numPairs;
            for (int i = 0; i < numPairs; i++) {
                removeOnePair();
            }
        }
    }

}
