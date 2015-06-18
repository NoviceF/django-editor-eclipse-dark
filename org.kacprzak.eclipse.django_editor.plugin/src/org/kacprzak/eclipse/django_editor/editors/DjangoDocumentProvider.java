package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;

/**
 * @author Zbigniew Kacprzak
*/
public class DjangoDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new DjangoPartitioner();
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}

class DjangoPartitioner extends FastPartitioner
{
	public DjangoPartitioner() {
		super(new DjangoPartitionScanner(), IDjangoPartitions.CONFIGURED_CONTENT_TYPES);
	}
	
	public void connect(IDocument document, boolean delayInitialise) {
		super.connect(document, delayInitialise);
//		printPartitions(document);
	}	
	
	public void printPartitions(IDocument document) {
		StringBuffer buffer = new StringBuffer();
		ITypedRegion[] partitions = computePartitioning(0, document.getLength());
		for (int i = 0; i < partitions.length; i++) {
			try {
				buffer.append("***************************\n");
				buffer.append("Partition type: " 
							+ partitions[i].getType() 
							+ ", offset: " + partitions[i].getOffset()
							+ ", length: " + partitions[i].getLength());
				buffer.append("\n");
				buffer.append("Text:\n");
				buffer.append(document.get(partitions[i].getOffset(), 
				partitions[i].getLength()));
				buffer.append("\n------------------------------------------------------\n");
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		System.out.print(buffer);
	}	
}