// Props Type

export interface ModalProps {
  modalItems : {
    title?: string,
    content: string,
    modalType: 'txtOnly' | 'warn' | 'done' | 'input' | 'ing',
    btn: number
  },

  onSubmitBtnClick?: () => void,
  onCancelBtnClick?: () => void
}


// API type