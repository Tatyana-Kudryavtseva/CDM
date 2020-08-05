// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See License.txt in the project root for license information.

package com.microsoft.commondatamodel.objectmodel.persistence.cdmfolder.projections;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.commondatamodel.objectmodel.cdm.CdmCorpusContext;
import com.microsoft.commondatamodel.objectmodel.cdm.CdmEntityReference;
import com.microsoft.commondatamodel.objectmodel.cdm.projections.*;
import com.microsoft.commondatamodel.objectmodel.enums.CdmObjectType;
import com.microsoft.commondatamodel.objectmodel.enums.CdmOperationType;
import com.microsoft.commondatamodel.objectmodel.persistence.cdmfolder.EntityReferencePersistence;
import com.microsoft.commondatamodel.objectmodel.persistence.cdmfolder.types.projections.*;
import com.microsoft.commondatamodel.objectmodel.utilities.CopyOptions;
import com.microsoft.commondatamodel.objectmodel.utilities.JMapper;
import com.microsoft.commondatamodel.objectmodel.utilities.ResolveOptions;
import com.microsoft.commondatamodel.objectmodel.utilities.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Projection persistence
 */
public class ProjectionPersistence {
    public static CdmProjection fromData(final CdmCorpusContext ctx, final JsonNode obj) {
        if (obj == null) {
            return null;
        }

        CdmProjection projection = ctx.getCorpus().makeObject(CdmObjectType.ProjectionDef);

        CdmEntityReference source = EntityReferencePersistence.fromData(ctx, obj.get("source"));

        if (obj.get("explanation") != null) {
            projection.setExplanation(obj.get("explanation").asText());
        }

        if (obj.get("condition") != null) {
            projection.setCondition(obj.get("condition").asText());
        }

        if (obj.get("operations") != null) {
            List<JsonNode> operationJsons = JMapper.MAP.convertValue(obj.get("operations"), new TypeReference<List<JsonNode>>() {
            });

            for (JsonNode operationJson : operationJsons) {
                String type = operationJson.get("$type").asText();
                switch (type) {
                    case "addCountAttribute":
                        CdmOperationAddCountAttribute addCountAttributeOp = OperationAddCountAttributePersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(addCountAttributeOp);
                        break;
                    case "addSupportingAttribute":
                        CdmOperationAddSupportingAttribute addSupportingAttributeOp = OperationAddSupportingAttributePersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(addSupportingAttributeOp);
                        break;
                    case "addTypeAttribute":
                        CdmOperationAddTypeAttribute addTypeAttributeOp = OperationAddTypeAttributePersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(addTypeAttributeOp);
                        break;
                    case "excludeAttributes":
                        CdmOperationExcludeAttributes excludeAttributesOp = OperationExcludeAttributesPersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(excludeAttributesOp);
                        break;
                    case "arrayExpansion":
                        CdmOperationArrayExpansion arrayExpansionOp = OperationArrayExpansionPersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(arrayExpansionOp);
                        break;
                    case "combineAttributes":
                        CdmOperationCombineAttributes combineAttributesOp = OperationCombineAttributesPersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(combineAttributesOp);
                        break;
                    case "renameAttributes":
                        CdmOperationRenameAttributes renameAttributesOp = OperationRenameAttributesPersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(renameAttributesOp);
                        break;
                    case "replaceAsForeignKey":
                        CdmOperationReplaceAsForeignKey replaceAsForeignKeyOp = OperationReplaceAsForeignKeyPersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(replaceAsForeignKeyOp);
                        break;
                    case "includeAttributes":
                        CdmOperationIncludeAttributes includeAttributesOp = OperationIncludeAttributesPersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(includeAttributesOp);
                        break;
                    case "addAttributeGroup":
                        CdmOperationAddAttributeGroup addAttributeGroupOp = OperationAddAttributeGroupPersistence.fromData(ctx, operationJson);
                        projection.getOperations().add(addAttributeGroupOp);
                        break;
                    default:
                        Logger.error(ProjectionPersistence.class.getSimpleName(), ctx, Logger.format("Invalid operation type '{0}'.", type), "fromData");
                        break;
                }
            }
        }

        projection.setSource(source);

        return projection;
    }

    public static Projection toData(final CdmProjection instance, final ResolveOptions resOpt, final CopyOptions options) {
        if (instance == null) {
            return null;
        }

        Object source = EntityReferencePersistence.toData(instance.getSource(), resOpt, options);

        List<OperationBase> operations = null;
        if (instance.getOperations() != null) {
            operations = new ArrayList<OperationBase>();
            for (CdmOperationBase operation : instance.getOperations()) {
                switch (operation.getObjectType()) {
                    case OperationAddCountAttributeDef:
                        OperationAddCountAttribute addCountAttributeOp = OperationAddCountAttributePersistence.toData((CdmOperationAddCountAttribute) operation, resOpt, options);
                        operations.add(addCountAttributeOp);
                        break;
                    case OperationAddSupportingAttributeDef:
                        OperationAddSupportingAttribute addSupportingAttributeOp = OperationAddSupportingAttributePersistence.toData((CdmOperationAddSupportingAttribute) operation, resOpt, options);
                        operations.add(addSupportingAttributeOp);
                        break;
                    case OperationAddTypeAttributeDef:
                        OperationAddTypeAttribute addTypeAttributeOp = OperationAddTypeAttributePersistence.toData((CdmOperationAddTypeAttribute) operation, resOpt, options);
                        operations.add(addTypeAttributeOp);
                        break;
                    case OperationExcludeAttributesDef:
                        OperationExcludeAttributes excludeAttributesOp = OperationExcludeAttributesPersistence.toData((CdmOperationExcludeAttributes) operation, resOpt, options);
                        operations.add(excludeAttributesOp);
                        break;
                    case OperationArrayExpansionDef:
                        OperationArrayExpansion arrayExpansionOp = OperationArrayExpansionPersistence.toData((CdmOperationArrayExpansion) operation, resOpt, options);
                        operations.add(arrayExpansionOp);
                        break;
                    case OperationCombineAttributesDef:
                        OperationCombineAttributes combineAttributesOp = OperationCombineAttributesPersistence.toData((CdmOperationCombineAttributes) operation, resOpt, options);
                        operations.add(combineAttributesOp);
                        break;
                    case OperationRenameAttributesDef:
                        OperationRenameAttributes renameAttributesOp = OperationRenameAttributesPersistence.toData((CdmOperationRenameAttributes) operation, resOpt, options);
                        operations.add(renameAttributesOp);
                        break;
                    case OperationReplaceAsForeignKeyDef:
                        OperationReplaceAsForeignKey replaceAsForeignKeyOp = OperationReplaceAsForeignKeyPersistence.toData((CdmOperationReplaceAsForeignKey) operation, resOpt, options);
                        operations.add(replaceAsForeignKeyOp);
                        break;
                    case OperationIncludeAttributesDef:
                        OperationIncludeAttributes includeAttributesOp = OperationIncludeAttributesPersistence.toData((CdmOperationIncludeAttributes) operation, resOpt, options);
                        operations.add(includeAttributesOp);
                        break;
                    case OperationAddAttributeGroupDef:
                        OperationAddAttributeGroup addAttributeGroupOp = OperationAddAttributeGroupPersistence.toData((CdmOperationAddAttributeGroup) operation, resOpt, options);
                        operations.add(addAttributeGroupOp);
                        break;
                    default:
                        OperationBase baseOp = new OperationBase();
                        baseOp.setType(OperationTypeConvertor.operationTypeToString(CdmOperationType.Error));
                        operations.add(baseOp);
                        break;
                }
            }
        }
        
        Projection obj = new Projection();
        obj.setExplanation(instance.getExplanation());
        obj.setSource(source);
        obj.setOperations(operations);
        obj.setCondition(instance.getCondition());

        return obj;
    }
}
