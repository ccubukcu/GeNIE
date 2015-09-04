function sectionLoaderFadeOut(subclass) {
	var classPath = ".loader.section-loader." + subclass;
	$(classPath).fadeOut('slow');
}

function sectionLoaderFadeIn(subclass) {
	var classPath = ".loader.section-loader." + subclass;
	$(classPath).fadeIn('slow');
}

function openModal(modalId) {
	$(modalId).modal('show');
}

function hideModal(modalId) {
	$(modalId).modal('hide');
}

function hideModalOnSuccessWithDropdown(args, modalId) {
	setDropDownCarat();
	
	hideModalOnSuccess(args, modalId);
}

function hideModalOnSuccess(args, modalId) {
	if(args.success) { 
		hideModal(modalId); 
		setPaginatorClasses();
	}
}

function openLayeredModal(foremostId, backgroundId) {
	$(backgroundId).css('opacity', .25);
	openModal(foremostId);
}

function hideLayeredModal(foremostId, backgroundId) {
	$(backgroundId).css('opacity', 1);
	hideModal(foremostId);
}

function openModalWithDropdown(modalId) {
	setDropDownCarat();
	setPaginatorClasses();
	openModal(modalId);
}

function setCurrentPage(pageId) {
	$("nav>ul>li.current_page_item").removeClass("current_page_item");
	$(pageId).addClass("current_page_item");
}

function setPaginatorClasses() {
	$(".ui-paginator-first").addClass('fa fa-fast-backward');
	$(".ui-paginator-prev").addClass('fa fa-chevron-left');
	$(".ui-paginator-next").addClass('fa fa-chevron-right');
	$(".ui-paginator-last").addClass('fa fa-fast-forward');
}

function setDropDownCarat() {
	$(".ui-selectonemenu-trigger .ui-icon").removeClass().addClass('carat');
}

$(function($) {
	setPaginatorClasses();
});	

$(function($) {
	setDropDownCarat();
});

