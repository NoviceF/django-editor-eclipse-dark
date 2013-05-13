package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
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
			IDocumentPartitioner partitioner = createPartitioner();
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
	private FastPartitioner createPartitioner() {
		return new FastPartitioner( new DjangoPartitionScanner(), IDjangoPartitions.CONFIGURED_CONTENT_TYPES);
	}
}
