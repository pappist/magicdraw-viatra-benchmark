package com.incquerylabs.instaschema.generator;

import com.nomagic.uml2.ext.jmi.reflect.AbstractRepository;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallOperationAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.InputPin;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OpaqueAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OutputPin;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.SendSignalAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.ValuePin;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.AcceptCallAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.AcceptEventAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.CreateLinkObjectAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.QualifierValue;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.ReadExtentAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.ReadIsClassifiedObjectAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.ReadLinkObjectEndAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.ReadLinkObjectEndQualifierAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.ReclassifyObjectAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.ReduceAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.ReplyAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.StartClassifierBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.StartObjectBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdcompleteactions.UnmarshallAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.AddStructuralFeatureValueAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.BroadcastSignalAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.ClearAssociationAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.ClearStructuralFeatureAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.CreateLinkAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.CreateObjectAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.DestroyLinkAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.DestroyObjectAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.LinkEndCreationData;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.LinkEndData;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.LinkEndDestructionData;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.ReadLinkAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.ReadSelfAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.ReadStructuralFeatureAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.RemoveStructuralFeatureValueAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.SendObjectAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.TestIdentityAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdintermediateactions.ValueSpecificationAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdstructuredactions.ActionInputPin;
import com.nomagic.uml2.ext.magicdraw.actions.mdstructuredactions.AddVariableValueAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdstructuredactions.ClearVariableAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdstructuredactions.RaiseExceptionAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdstructuredactions.ReadVariableAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdstructuredactions.RemoveVariableValueAction;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityFinalNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityParameterNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ControlFlow;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.InitialNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ObjectFlow;
import com.nomagic.uml2.ext.magicdraw.activities.mdcompleteactivities.DataStoreNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdcompleteactivities.InterruptibleActivityRegion;
import com.nomagic.uml2.ext.magicdraw.activities.mdcompleteactivities.ObjectNodeOrderingKind;
import com.nomagic.uml2.ext.magicdraw.activities.mdcompleteactivities.ObjectNodeOrderingKindEnum;
import com.nomagic.uml2.ext.magicdraw.activities.mdcompleteactivities.ParameterEffectKind;
import com.nomagic.uml2.ext.magicdraw.activities.mdcompleteactivities.ParameterEffectKindEnum;
import com.nomagic.uml2.ext.magicdraw.activities.mdcompleteactivities.ParameterSet;
import com.nomagic.uml2.ext.magicdraw.activities.mdextrastructuredactivities.ExceptionHandler;
import com.nomagic.uml2.ext.magicdraw.activities.mdextrastructuredactivities.ExpansionKind;
import com.nomagic.uml2.ext.magicdraw.activities.mdextrastructuredactivities.ExpansionKindEnum;
import com.nomagic.uml2.ext.magicdraw.activities.mdextrastructuredactivities.ExpansionNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdextrastructuredactivities.ExpansionRegion;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.CentralBufferNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.DecisionNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.FlowFinalNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ForkNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.JoinNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.MergeNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdstructuredactivities.Clause;
import com.nomagic.uml2.ext.magicdraw.activities.mdstructuredactivities.ConditionalNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdstructuredactivities.LoopNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdstructuredactivities.SequenceNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdstructuredactivities.StructuredActivityNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdstructuredactivities.Variable;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationFlow;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdinformationflows.InformationItem;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdmodels.Model;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.ClassifierTemplateParameter;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.ConnectableElementTemplateParameter;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.OperationTemplateParameter;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.RedefinableTemplateSignature;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.StringExpression;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.TemplateBinding;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.TemplateParameter;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.TemplateParameterSubstitution;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdtemplates.TemplateSignature;
import com.nomagic.uml2.ext.magicdraw.classes.mdassociationclasses.AssociationClass;
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Abstraction;
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Dependency;
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Realization;
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Substitution;
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Usage;
import com.nomagic.uml2.ext.magicdraw.classes.mdinterfaces.Interface;
import com.nomagic.uml2.ext.magicdraw.classes.mdinterfaces.InterfaceRealization;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.AggregationKind;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.AggregationKindEnum;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Comment;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementImport;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Expression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Generalization;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceSpecification;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralBoolean;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralNull;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralUnlimitedNatural;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.OpaqueExpression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Operation;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageImport;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageMerge;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ParameterDirectionKind;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ParameterDirectionKindEnum;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PrimitiveType;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Slot;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.VisibilityKind;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.VisibilityKindEnum;
import com.nomagic.uml2.ext.magicdraw.classes.mdpowertypes.GeneralizationSet;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdbasicbehaviors.FunctionBehavior;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdbasicbehaviors.OpaqueBehavior;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.AnyReceiveEvent;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.CallConcurrencyKind;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.CallConcurrencyKindEnum;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.CallEvent;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.ChangeEvent;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Reception;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Signal;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.SignalEvent;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.TimeEvent;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdcommunications.Trigger;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.Duration;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.DurationConstraint;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.DurationInterval;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.DurationObservation;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.Interval;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.IntervalConstraint;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.TimeConstraint;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.TimeExpression;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.TimeInterval;
import com.nomagic.uml2.ext.magicdraw.commonbehaviors.mdsimpletime.TimeObservation;
import com.nomagic.uml2.ext.magicdraw.components.mdbasiccomponents.Component;
import com.nomagic.uml2.ext.magicdraw.components.mdbasiccomponents.ComponentRealization;
import com.nomagic.uml2.ext.magicdraw.components.mdbasiccomponents.ConnectorKind;
import com.nomagic.uml2.ext.magicdraw.components.mdbasiccomponents.ConnectorKindEnum;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdcollaborations.Collaboration;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdcollaborations.CollaborationUse;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.ConnectorEnd;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port;
import com.nomagic.uml2.ext.magicdraw.deployments.mdartifacts.Artifact;
import com.nomagic.uml2.ext.magicdraw.deployments.mdartifacts.Manifestation;
import com.nomagic.uml2.ext.magicdraw.deployments.mdcomponentdeployments.DeploymentSpecification;
import com.nomagic.uml2.ext.magicdraw.deployments.mdnodes.CommunicationPath;
import com.nomagic.uml2.ext.magicdraw.deployments.mdnodes.Deployment;
import com.nomagic.uml2.ext.magicdraw.deployments.mdnodes.Device;
import com.nomagic.uml2.ext.magicdraw.deployments.mdnodes.ExecutionEnvironment;
import com.nomagic.uml2.ext.magicdraw.deployments.mdnodes.Node;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.ActionExecutionSpecification;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.BehaviorExecutionSpecification;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.DestructionOccurrenceSpecification;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.ExecutionOccurrenceSpecification;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.GeneralOrdering;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.Interaction;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.Lifeline;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.Message;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.MessageKind;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.MessageKindEnum;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.MessageOccurrenceSpecification;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.MessageSort;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.MessageSortEnum;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.OccurrenceSpecification;
import com.nomagic.uml2.ext.magicdraw.interactions.mdbasicinteractions.StateInvariant;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.CombinedFragment;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.ConsiderIgnoreFragment;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.Continuation;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.Gate;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.InteractionConstraint;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.InteractionOperand;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.InteractionOperatorKind;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.InteractionOperatorKindEnum;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.InteractionUse;
import com.nomagic.uml2.ext.magicdraw.interactions.mdfragments.PartDecomposition;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Extension;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.ExtensionEnd;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Image;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.ProfileApplication;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.ext.magicdraw.mdusecases.Actor;
import com.nomagic.uml2.ext.magicdraw.mdusecases.Extend;
import com.nomagic.uml2.ext.magicdraw.mdusecases.ExtensionPoint;
import com.nomagic.uml2.ext.magicdraw.mdusecases.Include;
import com.nomagic.uml2.ext.magicdraw.mdusecases.UseCase;
import com.nomagic.uml2.ext.magicdraw.metadata.UMLFactory;
import com.nomagic.uml2.ext.magicdraw.metadata.UMLPackage;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.ConnectionPointReference;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.FinalState;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Pseudostate;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.PseudostateKind;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.PseudostateKindEnum;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Region;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.StateMachine;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.TransitionKind;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.TransitionKindEnum;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdprotocolstatemachines.ProtocolConformance;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdprotocolstatemachines.ProtocolStateMachine;
import com.nomagic.uml2.ext.magicdraw.statemachines.mdprotocolstatemachines.ProtocolTransition;
import com.nomagic.uml2.impl.DummyRepository;
import com.nomagic.uml2.impl.TASRepositoryImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

public class FixedNSURIUMLFactory extends EFactoryImpl implements UMLFactory {
	private volatile AbstractRepository repository;

	public static synchronized UMLFactory init() {
		try {
			UMLFactory exception = (UMLFactory) Registry.INSTANCE
					.getEFactory("http://www.nomagic.com/magicdraw/UML/2.5.0");
			if (exception != null) {
				return exception;
			}
		} catch (Exception arg0) {
			EcorePlugin.INSTANCE.log(arg0);
		}

		return new FixedNSURIUMLFactory();
	}

	public void setRepository(AbstractRepository repository) {
		this.repository = repository;
	}

	public AbstractRepository getRepository() {
		return (AbstractRepository) (this.repository == null ? DummyRepository.DUMMY_REPOSITORY : this.repository);
	}

	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case 0:
			return this.createVariable();
		case 1:
		case 2:
		case 3:
		case 4:
		case 7:
		case 8:
		case 9:
		case 12:
		case 14:
		case 15:
		case 17:
		case 18:
		case 24:
		case 25:
		case 27:
		case 28:
		case 30:
		case 32:
		case 33:
		case 34:
		case 35:
		case 37:
		case 38:
		case 42:
		case 43:
		case 45:
		case 58:
		case 59:
		case 61:
		case 64:
		case 66:
		case 72:
		case 73:
		case 78:
		case 79:
		case 83:
		case 84:
		case 96:
		case 101:
		case 104:
		case 111:
		case 114:
		case 129:
		case 149:
		case 161:
		case 171:
		case 173:
		case 208:
		case 218:
		default:
			throw new IllegalArgumentException("The class \'" + eClass.getName() + "\' is not a valid classifier");
		case 5:
			return this.createComment();
		case 6:
			return this.createInstanceSpecification();
		case 10:
			return this.createTemplateParameter();
		case 11:
			return this.createTemplateSignature();
		case 13:
			return this.createTemplateBinding();
		case 16:
			return this.createInformationFlow();
		case 19:
			return this.createElementImport();
		case 20:
			return this.createProfile();
		case 21:
			return this.createPackage();
		case 22:
			return this.createStereotype();
		case 23:
			return this.createClass();
		case 26:
			return this.createParameter();
		case 29:
			return this.createLifeline();
		case 31:
			return this.createInteraction();
		case 36:
			return this.createActivity();
		case 39:
			return this.createActivityPartition();
		case 40:
			return this.createStructuredActivityNode();
		case 41:
			return this.createInputPin();
		case 44:
			return this.createState();
		case 46:
			return this.createRegion();
		case 47:
			return this.createStateMachine();
		case 48:
			return this.createPseudostate();
		case 49:
			return this.createConnectionPointReference();
		case 50:
			return this.createTransition();
		case 51:
			return this.createConstraint();
		case 52:
			return this.createExtend();
		case 53:
			return this.createUseCase();
		case 54:
			return this.createExtensionPoint();
		case 55:
			return this.createInclude();
		case 56:
			return this.createProtocolTransition();
		case 57:
			return this.createOperation();
		case 60:
			return this.createParameterSet();
		case 62:
			return this.createAssociation();
		case 63:
			return this.createProperty();
		case 65:
			return this.createSlot();
		case 67:
			return this.createDataType();
		case 68:
			return this.createInterface();
		case 69:
			return this.createReception();
		case 70:
			return this.createSignal();
		case 71:
			return this.createSignalEvent();
		case 74:
			return this.createTrigger();
		case 75:
			return this.createPort();
		case 76:
			return this.createProtocolStateMachine();
		case 77:
			return this.createProtocolConformance();
		case 80:
			return this.createConnector();
		case 81:
			return this.createConnectorEnd();
		case 82:
			return this.createMessage();
		case 85:
			return this.createAcceptEventAction();
		case 86:
			return this.createOutputPin();
		case 87:
			return this.createReadExtentAction();
		case 88:
			return this.createConditionalNode();
		case 89:
			return this.createClause();
		case 90:
			return this.createLoopNode();
		case 91:
			return this.createReduceAction();
		case 92:
			return this.createCreateObjectAction();
		case 93:
			return this.createUnmarshallAction();
		case 94:
			return this.createOpaqueAction();
		case 95:
			return this.createReadLinkAction();
		case 97:
			return this.createLinkEndData();
		case 98:
			return this.createQualifierValue();
		case 99:
			return this.createReadLinkObjectEndAction();
		case 100:
			return this.createAcceptCallAction();
		case 102:
			return this.createCreateLinkObjectAction();
		case 103:
			return this.createCreateLinkAction();
		case 105:
			return this.createLinkEndCreationData();
		case 106:
			return this.createReadStructuralFeatureAction();
		case 107:
			return this.createClearStructuralFeatureAction();
		case 108:
			return this.createValueSpecificationAction();
		case 109:
			return this.createTestIdentityAction();
		case 110:
			return this.createReadIsClassifiedObjectAction();
		case 112:
			return this.createReadSelfAction();
		case 113:
			return this.createReadVariableAction();
		case 115:
			return this.createReadLinkObjectEndQualifierAction();
		case 116:
			return this.createReplyAction();
		case 117:
			return this.createSendSignalAction();
		case 118:
			return this.createBroadcastSignalAction();
		case 119:
			return this.createInterfaceRealization();
		case 120:
			return this.createRealization();
		case 121:
			return this.createAbstraction();
		case 122:
			return this.createDependency();
		case 123:
			return this.createCollaborationUse();
		case 124:
			return this.createCollaboration();
		case 125:
			return this.createOpaqueExpression();
		case 126:
			return this.createComponent();
		case 127:
			return this.createComponentRealization();
		case 128:
			return this.createArtifact();
		case 130:
			return this.createDeployment();
		case 131:
			return this.createDeploymentSpecification();
		case 132:
			return this.createManifestation();
		case 133:
			return this.createInteractionUse();
		case 134:
			return this.createGate();
		case 135:
			return this.createCombinedFragment();
		case 136:
			return this.createInteractionOperand();
		case 137:
			return this.createInteractionConstraint();
		case 138:
			return this.createClearAssociationAction();
		case 139:
			return this.createOperationTemplateParameter();
		case 140:
			return this.createCallOperationAction();
		case 141:
			return this.createCallEvent();
		case 142:
			return this.createStateInvariant();
		case 143:
			return this.createExceptionHandler();
		case 144:
			return this.createSendObjectAction();
		case 145:
			return this.createAddStructuralFeatureValueAction();
		case 146:
			return this.createRemoveStructuralFeatureValueAction();
		case 147:
			return this.createStartObjectBehaviorAction();
		case 148:
			return this.createRemoveVariableValueAction();
		case 150:
			return this.createDestroyObjectAction();
		case 151:
			return this.createReclassifyObjectAction();
		case 152:
			return this.createRaiseExceptionAction();
		case 153:
			return this.createLinkEndDestructionData();
		case 154:
			return this.createDestroyLinkAction();
		case 155:
			return this.createAddVariableValueAction();
		case 156:
			return this.createStartClassifierBehaviorAction();
		case 157:
			return this.createInterruptibleActivityRegion();
		case 158:
			return this.createSequenceNode();
		case 159:
			return this.createActionInputPin();
		case 160:
			return this.createActionExecutionSpecification();
		case 162:
			return this.createOccurrenceSpecification();
		case 163:
			return this.createGeneralOrdering();
		case 164:
			return this.createExecutionOccurrenceSpecification();
		case 165:
			return this.createPartDecomposition();
		case 166:
			return this.createValuePin();
		case 167:
			return this.createInterval();
		case 168:
			return this.createIntervalConstraint();
		case 169:
			return this.createChangeEvent();
		case 170:
			return this.createJoinNode();
		case 172:
			return this.createDuration();
		case 174:
			return this.createTimeExpression();
		case 175:
			return this.createTimeInterval();
		case 176:
			return this.createTimeConstraint();
		case 177:
			return this.createTimeEvent();
		case 178:
			return this.createDurationInterval();
		case 179:
			return this.createDurationConstraint();
		case 180:
			return this.createExpression();
		case 181:
			return this.createActivityParameterNode();
		case 182:
			return this.createObjectFlow();
		case 183:
			return this.createDecisionNode();
		case 184:
			return this.createBehaviorExecutionSpecification();
		case 185:
			return this.createCallBehaviorAction();
		case 186:
			return this.createExtension();
		case 187:
			return this.createExtensionEnd();
		case 188:
			return this.createImage();
		case 189:
			return this.createPackageMerge();
		case 190:
			return this.createProfileApplication();
		case 191:
			return this.createPackageImport();
		case 192:
			return this.createDiagram();
		case 193:
			return this.createGeneralization();
		case 194:
			return this.createGeneralizationSet();
		case 195:
			return this.createRedefinableTemplateSignature();
		case 196:
			return this.createSubstitution();
		case 197:
			return this.createClassifierTemplateParameter();
		case 198:
			return this.createInformationItem();
		case 199:
			return this.createTemplateParameterSubstitution();
		case 200:
			return this.createInstanceValue();
		case 201:
			return this.createElementValue();
		case 202:
			return this.createStringExpression();
		case 203:
			return this.createConsiderIgnoreFragment();
		case 204:
			return this.createTimeObservation();
		case 205:
			return this.createDurationObservation();
		case 206:
			return this.createConnectableElementTemplateParameter();
		case 207:
			return this.createLiteralInteger();
		case 209:
			return this.createEnumerationLiteral();
		case 210:
			return this.createEnumeration();
		case 211:
			return this.createFinalState();
		case 212:
			return this.createInitialNode();
		case 213:
			return this.createFunctionBehavior();
		case 214:
			return this.createOpaqueBehavior();
		case 215:
			return this.createDestructionOccurrenceSpecification();
		case 216:
			return this.createMessageOccurrenceSpecification();
		case 217:
			return this.createLiteralString();
		case 219:
			return this.createExecutionEnvironment();
		case 220:
			return this.createNode();
		case 221:
			return this.createDataStoreNode();
		case 222:
			return this.createCentralBufferNode();
		case 223:
			return this.createActivityFinalNode();
		case 224:
			return this.createLiteralBoolean();
		case 225:
			return this.createModel();
		case 226:
			return this.createCommunicationPath();
		case 227:
			return this.createContinuation();
		case 228:
			return this.createExpansionNode();
		case 229:
			return this.createExpansionRegion();
		case 230:
			return this.createLiteralNull();
		case 231:
			return this.createLiteralUnlimitedNatural();
		case 232:
			return this.createLiteralReal();
		case 233:
			return this.createAnyReceiveEvent();
		case 234:
			return this.createForkNode();
		case 235:
			return this.createControlFlow();
		case 236:
			return this.createPrimitiveType();
		case 237:
			return this.createUsage();
		case 238:
			return this.createFlowFinalNode();
		case 239:
			return this.createActor();
		case 240:
			return this.createAssociationClass();
		case 241:
			return this.createDevice();
		case 242:
			return this.createMergeNode();
		case 243:
			return this.createClearVariableAction();
		}
	}

	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case 245:
			return this.createVisibilityKindFromString(eDataType, initialValue);
		case 246:
			return this.createPseudostateKindFromString(eDataType, initialValue);
		case 247:
			return this.createCallConcurrencyKindFromString(eDataType, initialValue);
		case 248:
			return this.createAggregationKindFromString(eDataType, initialValue);
		case 249:
			return this.createConnectorKindFromString(eDataType, initialValue);
		case 250:
			return this.createMessageKindFromString(eDataType, initialValue);
		case 251:
			return this.createMessageSortFromString(eDataType, initialValue);
		case 252:
			return this.createInteractionOperatorKindFromString(eDataType, initialValue);
		case 253:
			return this.createTransitionKindFromString(eDataType, initialValue);
		case 254:
			return this.createObjectNodeOrderingKindFromString(eDataType, initialValue);
		case 255:
			return this.createParameterDirectionKindFromString(eDataType, initialValue);
		case 256:
			return this.createParameterEffectKindFromString(eDataType, initialValue);
		case 257:
			return this.createExpansionKindFromString(eDataType, initialValue);
		case 258:
			return this.createStringFromString(eDataType, initialValue);
		case 259:
			return this.createBooleanFromString(eDataType, initialValue);
		case 260:
			return this.createIntegerFromString(eDataType, initialValue);
		case 261:
			return this.createUnlimitedNaturalFromString(eDataType, initialValue);
		case 262:
			return this.createRealFromString(eDataType, initialValue);
		case 263:
			return this.createParameterParameterEffectKindFromString(eDataType, initialValue);
		case 264:
			return this.createNamedElementVisibilityKindFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException(
					"The datatype \'" + eDataType.getName() + "\' is not a valid classifier");
		}
	}

	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case 245:
			return this.convertVisibilityKindToString(eDataType, instanceValue);
		case 246:
			return this.convertPseudostateKindToString(eDataType, instanceValue);
		case 247:
			return this.convertCallConcurrencyKindToString(eDataType, instanceValue);
		case 248:
			return this.convertAggregationKindToString(eDataType, instanceValue);
		case 249:
			return this.convertConnectorKindToString(eDataType, instanceValue);
		case 250:
			return this.convertMessageKindToString(eDataType, instanceValue);
		case 251:
			return this.convertMessageSortToString(eDataType, instanceValue);
		case 252:
			return this.convertInteractionOperatorKindToString(eDataType, instanceValue);
		case 253:
			return this.convertTransitionKindToString(eDataType, instanceValue);
		case 254:
			return this.convertObjectNodeOrderingKindToString(eDataType, instanceValue);
		case 255:
			return this.convertParameterDirectionKindToString(eDataType, instanceValue);
		case 256:
			return this.convertParameterEffectKindToString(eDataType, instanceValue);
		case 257:
			return this.convertExpansionKindToString(eDataType, instanceValue);
		case 258:
			return this.convertStringToString(eDataType, instanceValue);
		case 259:
			return this.convertBooleanToString(eDataType, instanceValue);
		case 260:
			return this.convertIntegerToString(eDataType, instanceValue);
		case 261:
			return this.convertUnlimitedNaturalToString(eDataType, instanceValue);
		case 262:
			return this.convertRealToString(eDataType, instanceValue);
		case 263:
			return this.convertParameterParameterEffectKindToString(eDataType, instanceValue);
		case 264:
			return this.convertNamedElementVisibilityKindToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException(
					"The datatype \'" + eDataType.getName() + "\' is not a valid classifier");
		}
	}

	public SendSignalAction createSendSignalAction() {
		return this.getRepository().getElementsFactory().createSendSignalActionInstance();
	}

	public Comment createComment() {
		return this.getRepository().getElementsFactory().createCommentInstance();
	}

	public InstanceSpecification createInstanceSpecification() {
		return this.getRepository().getElementsFactory().createInstanceSpecificationInstance();
	}

	public TemplateParameter createTemplateParameter() {
		return this.getRepository().getElementsFactory().createTemplateParameterInstance();
	}

	public TemplateSignature createTemplateSignature() {
		return this.getRepository().getElementsFactory().createTemplateSignatureInstance();
	}

	public TemplateBinding createTemplateBinding() {
		return this.getRepository().getElementsFactory().createTemplateBindingInstance();
	}

	public InformationFlow createInformationFlow() {
		return this.getRepository().getElementsFactory().createInformationFlowInstance();
	}

	public ElementImport createElementImport() {
		return this.getRepository().getElementsFactory().createElementImportInstance();
	}

	public Profile createProfile() {
		return this.getRepository().getElementsFactory().createProfileInstance();
	}

	public Package createPackage() {
		return this.getRepository().getElementsFactory().createPackageInstance();
	}

	public Stereotype createStereotype() {
		return this.getRepository().getElementsFactory().createStereotypeInstance();
	}

	public Class createClass() {
		return this.getRepository().getElementsFactory().createClassInstance();
	}

	public Parameter createParameter() {
		return this.getRepository().getElementsFactory().createParameterInstance();
	}

	public Association createAssociation() {
		return this.getRepository().getElementsFactory().createAssociationInstance();
	}

	public Property createProperty() {
		return this.getRepository().getElementsFactory().createPropertyInstance();
	}

	public Slot createSlot() {
		return this.getRepository().getElementsFactory().createSlotInstance();
	}

	public InputPin createInputPin() {
		return this.getRepository().getElementsFactory().createInputPinInstance();
	}

	public State createState() {
		return this.getRepository().getElementsFactory().createStateInstance();
	}

	public Region createRegion() {
		return this.getRepository().getElementsFactory().createRegionInstance();
	}

	public StateMachine createStateMachine() {
		return this.getRepository().getElementsFactory().createStateMachineInstance();
	}

	public Pseudostate createPseudostate() {
		return this.getRepository().getElementsFactory().createPseudostateInstance();
	}

	public ConnectionPointReference createConnectionPointReference() {
		return this.getRepository().getElementsFactory().createConnectionPointReferenceInstance();
	}

	public Transition createTransition() {
		return this.getRepository().getElementsFactory().createTransitionInstance();
	}

	public Constraint createConstraint() {
		return this.getRepository().getElementsFactory().createConstraintInstance();
	}

	public Operation createOperation() {
		return this.getRepository().getElementsFactory().createOperationInstance();
	}

	public ParameterSet createParameterSet() {
		return this.getRepository().getElementsFactory().createParameterSetInstance();
	}

	public DataType createDataType() {
		return this.getRepository().getElementsFactory().createDataTypeInstance();
	}

	public Interface createInterface() {
		return this.getRepository().getElementsFactory().createInterfaceInstance();
	}

	public Reception createReception() {
		return this.getRepository().getElementsFactory().createReceptionInstance();
	}

	public Signal createSignal() {
		return this.getRepository().getElementsFactory().createSignalInstance();
	}

	public BroadcastSignalAction createBroadcastSignalAction() {
		return this.getRepository().getElementsFactory().createBroadcastSignalActionInstance();
	}

	public SignalEvent createSignalEvent() {
		return this.getRepository().getElementsFactory().createSignalEventInstance();
	}

	public Trigger createTrigger() {
		return this.getRepository().getElementsFactory().createTriggerInstance();
	}

	public Port createPort() {
		return this.getRepository().getElementsFactory().createPortInstance();
	}

	public ProtocolStateMachine createProtocolStateMachine() {
		return this.getRepository().getElementsFactory().createProtocolStateMachineInstance();
	}

	public ProtocolConformance createProtocolConformance() {
		return this.getRepository().getElementsFactory().createProtocolConformanceInstance();
	}

	public Connector createConnector() {
		return this.getRepository().getElementsFactory().createConnectorInstance();
	}

	public ConnectorEnd createConnectorEnd() {
		return this.getRepository().getElementsFactory().createConnectorEndInstance();
	}

	public ConnectableElementTemplateParameter createConnectableElementTemplateParameter() {
		return this.getRepository().getElementsFactory().createConnectableElementTemplateParameterInstance();
	}

	public Collaboration createCollaboration() {
		return this.getRepository().getElementsFactory().createCollaborationInstance();
	}

	public CollaborationUse createCollaborationUse() {
		return this.getRepository().getElementsFactory().createCollaborationUseInstance();
	}

	public Dependency createDependency() {
		return this.getRepository().getElementsFactory().createDependencyInstance();
	}

	public Lifeline createLifeline() {
		return this.getRepository().getElementsFactory().createLifelineInstance();
	}

	public Interaction createInteraction() {
		return this.getRepository().getElementsFactory().createInteractionInstance();
	}

	public Gate createGate() {
		return this.getRepository().getElementsFactory().createGateInstance();
	}

	public Message createMessage() {
		return this.getRepository().getElementsFactory().createMessageInstance();
	}

	public InteractionUse createInteractionUse() {
		return this.getRepository().getElementsFactory().createInteractionUseInstance();
	}

	public CombinedFragment createCombinedFragment() {
		return this.getRepository().getElementsFactory().createCombinedFragmentInstance();
	}

	public InteractionOperand createInteractionOperand() {
		return this.getRepository().getElementsFactory().createInteractionOperandInstance();
	}

	public InteractionConstraint createInteractionConstraint() {
		return this.getRepository().getElementsFactory().createInteractionConstraintInstance();
	}

	public GeneralOrdering createGeneralOrdering() {
		return this.getRepository().getElementsFactory().createGeneralOrderingInstance();
	}

	public OccurrenceSpecification createOccurrenceSpecification() {
		return this.getRepository().getElementsFactory().createOccurrenceSpecificationInstance();
	}

	public ExecutionOccurrenceSpecification createExecutionOccurrenceSpecification() {
		return this.getRepository().getElementsFactory().createExecutionOccurrenceSpecificationInstance();
	}

	public PartDecomposition createPartDecomposition() {
		return this.getRepository().getElementsFactory().createPartDecompositionInstance();
	}

	public StateInvariant createStateInvariant() {
		return this.getRepository().getElementsFactory().createStateInvariantInstance();
	}

	public ReplyAction createReplyAction() {
		return this.getRepository().getElementsFactory().createReplyActionInstance();
	}

	public AcceptEventAction createAcceptEventAction() {
		return this.getRepository().getElementsFactory().createAcceptEventActionInstance();
	}

	public OutputPin createOutputPin() {
		return this.getRepository().getElementsFactory().createOutputPinInstance();
	}

	public Clause createClause() {
		return this.getRepository().getElementsFactory().createClauseInstance();
	}

	public ConditionalNode createConditionalNode() {
		return this.getRepository().getElementsFactory().createConditionalNodeInstance();
	}

	public StructuredActivityNode createStructuredActivityNode() {
		return this.getRepository().getElementsFactory().createStructuredActivityNodeInstance();
	}

	public Activity createActivity() {
		return this.getRepository().getElementsFactory().createActivityInstance();
	}

	public ActivityPartition createActivityPartition() {
		return this.getRepository().getElementsFactory().createActivityPartitionInstance();
	}

	public Variable createVariable() {
		return this.getRepository().getElementsFactory().createVariableInstance();
	}

	public InterruptibleActivityRegion createInterruptibleActivityRegion() {
		return this.getRepository().getElementsFactory().createInterruptibleActivityRegionInstance();
	}

	public LoopNode createLoopNode() {
		return this.getRepository().getElementsFactory().createLoopNodeInstance();
	}

	public OpaqueAction createOpaqueAction() {
		return this.getRepository().getElementsFactory().createOpaqueActionInstance();
	}

	public ClearStructuralFeatureAction createClearStructuralFeatureAction() {
		return this.getRepository().getElementsFactory().createClearStructuralFeatureActionInstance();
	}

	public CreateLinkObjectAction createCreateLinkObjectAction() {
		return this.getRepository().getElementsFactory().createCreateLinkObjectActionInstance();
	}

	public CreateLinkAction createCreateLinkAction() {
		return this.getRepository().getElementsFactory().createCreateLinkActionInstance();
	}

	public LinkEndData createLinkEndData() {
		return this.getRepository().getElementsFactory().createLinkEndDataInstance();
	}

	public QualifierValue createQualifierValue() {
		return this.getRepository().getElementsFactory().createQualifierValueInstance();
	}

	public LinkEndCreationData createLinkEndCreationData() {
		return this.getRepository().getElementsFactory().createLinkEndCreationDataInstance();
	}

	public CreateObjectAction createCreateObjectAction() {
		return this.getRepository().getElementsFactory().createCreateObjectActionInstance();
	}

	public ReadExtentAction createReadExtentAction() {
		return this.getRepository().getElementsFactory().createReadExtentActionInstance();
	}

	public ReadIsClassifiedObjectAction createReadIsClassifiedObjectAction() {
		return this.getRepository().getElementsFactory().createReadIsClassifiedObjectActionInstance();
	}

	public ReadLinkAction createReadLinkAction() {
		return this.getRepository().getElementsFactory().createReadLinkActionInstance();
	}

	public ReadLinkObjectEndAction createReadLinkObjectEndAction() {
		return this.getRepository().getElementsFactory().createReadLinkObjectEndActionInstance();
	}

	public ReadLinkObjectEndQualifierAction createReadLinkObjectEndQualifierAction() {
		return this.getRepository().getElementsFactory().createReadLinkObjectEndQualifierActionInstance();
	}

	public ReadSelfAction createReadSelfAction() {
		return this.getRepository().getElementsFactory().createReadSelfActionInstance();
	}

	public ReadStructuralFeatureAction createReadStructuralFeatureAction() {
		return this.getRepository().getElementsFactory().createReadStructuralFeatureActionInstance();
	}

	public ReadVariableAction createReadVariableAction() {
		return this.getRepository().getElementsFactory().createReadVariableActionInstance();
	}

	public ReduceAction createReduceAction() {
		return this.getRepository().getElementsFactory().createReduceActionInstance();
	}

	public TestIdentityAction createTestIdentityAction() {
		return this.getRepository().getElementsFactory().createTestIdentityActionInstance();
	}

	public UnmarshallAction createUnmarshallAction() {
		return this.getRepository().getElementsFactory().createUnmarshallActionInstance();
	}

	public ValueSpecificationAction createValueSpecificationAction() {
		return this.getRepository().getElementsFactory().createValueSpecificationActionInstance();
	}

	public AcceptCallAction createAcceptCallAction() {
		return this.getRepository().getElementsFactory().createAcceptCallActionInstance();
	}

	public InterfaceRealization createInterfaceRealization() {
		return this.getRepository().getElementsFactory().createInterfaceRealizationInstance();
	}

	public Realization createRealization() {
		return this.getRepository().getElementsFactory().createRealizationInstance();
	}

	public Abstraction createAbstraction() {
		return this.getRepository().getElementsFactory().createAbstractionInstance();
	}

	public OpaqueExpression createOpaqueExpression() {
		return this.getRepository().getElementsFactory().createOpaqueExpressionInstance();
	}

	public Component createComponent() {
		return this.getRepository().getElementsFactory().createComponentInstance();
	}

	public ComponentRealization createComponentRealization() {
		return this.getRepository().getElementsFactory().createComponentRealizationInstance();
	}

	public OperationTemplateParameter createOperationTemplateParameter() {
		return this.getRepository().getElementsFactory().createOperationTemplateParameterInstance();
	}

	public CallEvent createCallEvent() {
		return this.getRepository().getElementsFactory().createCallEventInstance();
	}

	public CallOperationAction createCallOperationAction() {
		return this.getRepository().getElementsFactory().createCallOperationActionInstance();
	}

	public Artifact createArtifact() {
		return this.getRepository().getElementsFactory().createArtifactInstance();
	}

	public Deployment createDeployment() {
		return this.getRepository().getElementsFactory().createDeploymentInstance();
	}

	public DeploymentSpecification createDeploymentSpecification() {
		return this.getRepository().getElementsFactory().createDeploymentSpecificationInstance();
	}

	public Manifestation createManifestation() {
		return this.getRepository().getElementsFactory().createManifestationInstance();
	}

	public ProtocolTransition createProtocolTransition() {
		return this.getRepository().getElementsFactory().createProtocolTransitionInstance();
	}

	public Extend createExtend() {
		return this.getRepository().getElementsFactory().createExtendInstance();
	}

	public UseCase createUseCase() {
		return this.getRepository().getElementsFactory().createUseCaseInstance();
	}

	public ExtensionPoint createExtensionPoint() {
		return this.getRepository().getElementsFactory().createExtensionPointInstance();
	}

	public Include createInclude() {
		return this.getRepository().getElementsFactory().createIncludeInstance();
	}

	public ExceptionHandler createExceptionHandler() {
		return this.getRepository().getElementsFactory().createExceptionHandlerInstance();
	}

	public LinkEndDestructionData createLinkEndDestructionData() {
		return this.getRepository().getElementsFactory().createLinkEndDestructionDataInstance();
	}

	public DestroyLinkAction createDestroyLinkAction() {
		return this.getRepository().getElementsFactory().createDestroyLinkActionInstance();
	}

	public RaiseExceptionAction createRaiseExceptionAction() {
		return this.getRepository().getElementsFactory().createRaiseExceptionActionInstance();
	}

	public AddStructuralFeatureValueAction createAddStructuralFeatureValueAction() {
		return this.getRepository().getElementsFactory().createAddStructuralFeatureValueActionInstance();
	}

	public AddVariableValueAction createAddVariableValueAction() {
		return this.getRepository().getElementsFactory().createAddVariableValueActionInstance();
	}

	public ClearAssociationAction createClearAssociationAction() {
		return this.getRepository().getElementsFactory().createClearAssociationActionInstance();
	}

	public ReclassifyObjectAction createReclassifyObjectAction() {
		return this.getRepository().getElementsFactory().createReclassifyObjectActionInstance();
	}

	public StartClassifierBehaviorAction createStartClassifierBehaviorAction() {
		return this.getRepository().getElementsFactory().createStartClassifierBehaviorActionInstance();
	}

	public StartObjectBehaviorAction createStartObjectBehaviorAction() {
		return this.getRepository().getElementsFactory().createStartObjectBehaviorActionInstance();
	}

	public RemoveStructuralFeatureValueAction createRemoveStructuralFeatureValueAction() {
		return this.getRepository().getElementsFactory().createRemoveStructuralFeatureValueActionInstance();
	}

	public RemoveVariableValueAction createRemoveVariableValueAction() {
		return this.getRepository().getElementsFactory().createRemoveVariableValueActionInstance();
	}

	public SendObjectAction createSendObjectAction() {
		return this.getRepository().getElementsFactory().createSendObjectActionInstance();
	}

	public DestroyObjectAction createDestroyObjectAction() {
		return this.getRepository().getElementsFactory().createDestroyObjectActionInstance();
	}

	public ChangeEvent createChangeEvent() {
		return this.getRepository().getElementsFactory().createChangeEventInstance();
	}

	public Duration createDuration() {
		return this.getRepository().getElementsFactory().createDurationInstance();
	}

	public TimeExpression createTimeExpression() {
		return this.getRepository().getElementsFactory().createTimeExpressionInstance();
	}

	public TimeInterval createTimeInterval() {
		return this.getRepository().getElementsFactory().createTimeIntervalInstance();
	}

	public Interval createInterval() {
		return this.getRepository().getElementsFactory().createIntervalInstance();
	}

	public IntervalConstraint createIntervalConstraint() {
		return this.getRepository().getElementsFactory().createIntervalConstraintInstance();
	}

	public TimeConstraint createTimeConstraint() {
		return this.getRepository().getElementsFactory().createTimeConstraintInstance();
	}

	public TimeEvent createTimeEvent() {
		return this.getRepository().getElementsFactory().createTimeEventInstance();
	}

	public DurationInterval createDurationInterval() {
		return this.getRepository().getElementsFactory().createDurationIntervalInstance();
	}

	public DurationConstraint createDurationConstraint() {
		return this.getRepository().getElementsFactory().createDurationConstraintInstance();
	}

	public JoinNode createJoinNode() {
		return this.getRepository().getElementsFactory().createJoinNodeInstance();
	}

	public Expression createExpression() {
		return this.getRepository().getElementsFactory().createExpressionInstance();
	}

	public ValuePin createValuePin() {
		return this.getRepository().getElementsFactory().createValuePinInstance();
	}

	public ActivityParameterNode createActivityParameterNode() {
		return this.getRepository().getElementsFactory().createActivityParameterNodeInstance();
	}

	public BehaviorExecutionSpecification createBehaviorExecutionSpecification() {
		return this.getRepository().getElementsFactory().createBehaviorExecutionSpecificationInstance();
	}

	public CallBehaviorAction createCallBehaviorAction() {
		return this.getRepository().getElementsFactory().createCallBehaviorActionInstance();
	}

	public DecisionNode createDecisionNode() {
		return this.getRepository().getElementsFactory().createDecisionNodeInstance();
	}

	public ObjectFlow createObjectFlow() {
		return this.getRepository().getElementsFactory().createObjectFlowInstance();
	}

	public Extension createExtension() {
		return this.getRepository().getElementsFactory().createExtensionInstance();
	}

	public ExtensionEnd createExtensionEnd() {
		return this.getRepository().getElementsFactory().createExtensionEndInstance();
	}

	public Image createImage() {
		return this.getRepository().getElementsFactory().createImageInstance();
	}

	public PackageMerge createPackageMerge() {
		return this.getRepository().getElementsFactory().createPackageMergeInstance();
	}

	public ProfileApplication createProfileApplication() {
		return this.getRepository().getElementsFactory().createProfileApplicationInstance();
	}

	public PackageImport createPackageImport() {
		return this.getRepository().getElementsFactory().createPackageImportInstance();
	}

	public Diagram createDiagram() {
		return this.getRepository().getElementsFactory().createDiagramInstance();
	}

	public Generalization createGeneralization() {
		return this.getRepository().getElementsFactory().createGeneralizationInstance();
	}

	public GeneralizationSet createGeneralizationSet() {
		return this.getRepository().getElementsFactory().createGeneralizationSetInstance();
	}

	public RedefinableTemplateSignature createRedefinableTemplateSignature() {
		return this.getRepository().getElementsFactory().createRedefinableTemplateSignatureInstance();
	}

	public Substitution createSubstitution() {
		return this.getRepository().getElementsFactory().createSubstitutionInstance();
	}

	public ClassifierTemplateParameter createClassifierTemplateParameter() {
		return this.getRepository().getElementsFactory().createClassifierTemplateParameterInstance();
	}

	public InformationItem createInformationItem() {
		return this.getRepository().getElementsFactory().createInformationItemInstance();
	}

	public TemplateParameterSubstitution createTemplateParameterSubstitution() {
		return this.getRepository().getElementsFactory().createTemplateParameterSubstitutionInstance();
	}

	public InstanceValue createInstanceValue() {
		return this.getRepository().getElementsFactory().createInstanceValueInstance();
	}

	public ElementValue createElementValue() {
		return this.getRepository().getElementsFactory().createElementValueInstance();
	}

	public StringExpression createStringExpression() {
		return this.getRepository().getElementsFactory().createStringExpressionInstance();
	}

	public DurationObservation createDurationObservation() {
		return this.getRepository().getElementsFactory().createDurationObservationInstance();
	}

	public TimeObservation createTimeObservation() {
		return this.getRepository().getElementsFactory().createTimeObservationInstance();
	}

	public ConsiderIgnoreFragment createConsiderIgnoreFragment() {
		return this.getRepository().getElementsFactory().createConsiderIgnoreFragmentInstance();
	}

	public SequenceNode createSequenceNode() {
		return this.getRepository().getElementsFactory().createSequenceNodeInstance();
	}

	public ActionExecutionSpecification createActionExecutionSpecification() {
		return this.getRepository().getElementsFactory().createActionExecutionSpecificationInstance();
	}

	public ActionInputPin createActionInputPin() {
		return this.getRepository().getElementsFactory().createActionInputPinInstance();
	}

	public LiteralString createLiteralString() {
		return this.getRepository().getElementsFactory().createLiteralStringInstance();
	}

	public DataStoreNode createDataStoreNode() {
		return this.getRepository().getElementsFactory().createDataStoreNodeInstance();
	}

	public CentralBufferNode createCentralBufferNode() {
		return this.getRepository().getElementsFactory().createCentralBufferNodeInstance();
	}

	public InitialNode createInitialNode() {
		return this.getRepository().getElementsFactory().createInitialNodeInstance();
	}

	public Device createDevice() {
		return this.getRepository().getElementsFactory().createDeviceInstance();
	}

	public Node createNode() {
		return this.getRepository().getElementsFactory().createNodeInstance();
	}

	public AssociationClass createAssociationClass() {
		return this.getRepository().getElementsFactory().createAssociationClassInstance();
	}

	public Actor createActor() {
		return this.getRepository().getElementsFactory().createActorInstance();
	}

	public ExecutionEnvironment createExecutionEnvironment() {
		return this.getRepository().getElementsFactory().createExecutionEnvironmentInstance();
	}

	public FunctionBehavior createFunctionBehavior() {
		return this.getRepository().getElementsFactory().createFunctionBehaviorInstance();
	}

	public OpaqueBehavior createOpaqueBehavior() {
		return this.getRepository().getElementsFactory().createOpaqueBehaviorInstance();
	}

	public ActivityFinalNode createActivityFinalNode() {
		return this.getRepository().getElementsFactory().createActivityFinalNodeInstance();
	}

	public FlowFinalNode createFlowFinalNode() {
		return this.getRepository().getElementsFactory().createFlowFinalNodeInstance();
	}

	public LiteralReal createLiteralReal() {
		return this.getRepository().getElementsFactory().createLiteralRealInstance();
	}

	public ForkNode createForkNode() {
		return this.getRepository().getElementsFactory().createForkNodeInstance();
	}

	public ControlFlow createControlFlow() {
		return this.getRepository().getElementsFactory().createControlFlowInstance();
	}

	public Usage createUsage() {
		return this.getRepository().getElementsFactory().createUsageInstance();
	}

	public LiteralUnlimitedNatural createLiteralUnlimitedNatural() {
		return this.getRepository().getElementsFactory().createLiteralUnlimitedNaturalInstance();
	}

	public LiteralInteger createLiteralInteger() {
		return this.getRepository().getElementsFactory().createLiteralIntegerInstance();
	}

	public Enumeration createEnumeration() {
		return this.getRepository().getElementsFactory().createEnumerationInstance();
	}

	public EnumerationLiteral createEnumerationLiteral() {
		return this.getRepository().getElementsFactory().createEnumerationLiteralInstance();
	}

	public ExpansionNode createExpansionNode() {
		return this.getRepository().getElementsFactory().createExpansionNodeInstance();
	}

	public ExpansionRegion createExpansionRegion() {
		return this.getRepository().getElementsFactory().createExpansionRegionInstance();
	}

	public CommunicationPath createCommunicationPath() {
		return this.getRepository().getElementsFactory().createCommunicationPathInstance();
	}

	public PrimitiveType createPrimitiveType() {
		return this.getRepository().getElementsFactory().createPrimitiveTypeInstance();
	}

	public FinalState createFinalState() {
		return this.getRepository().getElementsFactory().createFinalStateInstance();
	}

	public AnyReceiveEvent createAnyReceiveEvent() {
		return this.getRepository().getElementsFactory().createAnyReceiveEventInstance();
	}

	public MergeNode createMergeNode() {
		return this.getRepository().getElementsFactory().createMergeNodeInstance();
	}

	public Continuation createContinuation() {
		return this.getRepository().getElementsFactory().createContinuationInstance();
	}

	public LiteralNull createLiteralNull() {
		return this.getRepository().getElementsFactory().createLiteralNullInstance();
	}

	public MessageOccurrenceSpecification createMessageOccurrenceSpecification() {
		return this.getRepository().getElementsFactory().createMessageOccurrenceSpecificationInstance();
	}

	public LiteralBoolean createLiteralBoolean() {
		return this.getRepository().getElementsFactory().createLiteralBooleanInstance();
	}

	public DestructionOccurrenceSpecification createDestructionOccurrenceSpecification() {
		return this.getRepository().getElementsFactory().createDestructionOccurrenceSpecificationInstance();
	}

	public Model createModel() {
		return this.getRepository().getElementsFactory().createModelInstance();
	}

	public ClearVariableAction createClearVariableAction() {
		return this.getRepository().getElementsFactory().createClearVariableActionInstance();
	}

	public VisibilityKind createVisibilityKindFromString(EDataType eDataType, String initialValue) {
		VisibilityKindEnum result = VisibilityKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertVisibilityKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public PseudostateKind createPseudostateKindFromString(EDataType eDataType, String initialValue) {
		PseudostateKindEnum result = PseudostateKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertPseudostateKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public CallConcurrencyKind createCallConcurrencyKindFromString(EDataType eDataType, String initialValue) {
		CallConcurrencyKindEnum result = CallConcurrencyKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertCallConcurrencyKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public MessageKind createMessageKindFromString(EDataType eDataType, String initialValue) {
		MessageKindEnum result = MessageKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertMessageKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public MessageSort createMessageSortFromString(EDataType eDataType, String initialValue) {
		MessageSortEnum result = MessageSortEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertMessageSortToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public InteractionOperatorKind createInteractionOperatorKindFromString(EDataType eDataType, String initialValue) {
		InteractionOperatorKindEnum result = InteractionOperatorKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertInteractionOperatorKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public ConnectorKind createConnectorKindFromString(EDataType eDataType, String initialValue) {
		ConnectorKindEnum result = ConnectorKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertConnectorKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public TransitionKind createTransitionKindFromString(EDataType eDataType, String initialValue) {
		TransitionKindEnum result = TransitionKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertTransitionKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public ObjectNodeOrderingKind createObjectNodeOrderingKindFromString(EDataType eDataType, String initialValue) {
		ObjectNodeOrderingKindEnum result = ObjectNodeOrderingKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertObjectNodeOrderingKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public AggregationKind createAggregationKindFromString(EDataType eDataType, String initialValue) {
		AggregationKindEnum result = AggregationKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertAggregationKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public ParameterDirectionKind createParameterDirectionKindFromString(EDataType eDataType, String initialValue) {
		ParameterDirectionKindEnum result = ParameterDirectionKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertParameterDirectionKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public ParameterEffectKind createParameterEffectKindFromString(EDataType eDataType, String initialValue) {
		ParameterEffectKindEnum result = ParameterEffectKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertParameterEffectKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public ExpansionKind createExpansionKindFromString(EDataType eDataType, String initialValue) {
		ExpansionKindEnum result = ExpansionKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertExpansionKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public String createStringFromString(EDataType eDataType, String initialValue) {
		return (String) super.createFromString(eDataType, initialValue);
	}

	public String convertStringToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	public Boolean createBooleanFromString(EDataType eDataType, String initialValue) {
		return (Boolean) super.createFromString(eDataType, initialValue);
	}

	public String convertBooleanToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	public Integer createIntegerFromString(EDataType eDataType, String initialValue) {
		return (Integer) super.createFromString(eDataType, initialValue);
	}

	public String convertIntegerToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	public Integer createUnlimitedNaturalFromString(EDataType eDataType, String initialValue) {
		return (Integer) super.createFromString(eDataType, initialValue);
	}

	public String convertUnlimitedNaturalToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	public Double createRealFromString(EDataType eDataType, String initialValue) {
		return (Double) super.createFromString(eDataType, initialValue);
	}

	public String convertRealToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	public VisibilityKind createNamedElementVisibilityKindFromString(EDataType eDataType, String initialValue) {
		VisibilityKindEnum result = VisibilityKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertNamedElementVisibilityKindToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	public ParameterEffectKind createParameterParameterEffectKindFromString(EDataType eDataType, String initialValue) {
		ParameterEffectKindEnum result = ParameterEffectKindEnum.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value \'" + initialValue + "\' is not a valid enumerator of \'" + eDataType.getName() + "\'");
		} else {
			return result;
		}
	}

	public String convertParameterParameterEffectKindToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	public UMLPackage getUMLPackage() {
		return (UMLPackage) this.getEPackage();
	}

	public static UMLPackage getPackage() {
		return UMLPackage.eINSTANCE;
	}

	public static TASRepositoryImpl getDummyRepository() {
		return DummyRepository.DUMMY_REPOSITORY;
	}
}